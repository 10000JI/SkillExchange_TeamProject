package place.skillexchange.backend.auth.services;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import place.skillexchange.backend.entity.RefreshToken;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.UserUnAuthorizedException;
import place.skillexchange.backend.repository.RefreshTokenRepository;
import place.skillexchange.backend.repository.UserRepository;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class AuthFilterService extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;


    /**
     * 헤더에 토큰을 저장하는데, 저장된 토큰을 헤더에서 꺼내서 유효한지 검증하는 작업, 실패시 filer 작용으로 (security) exceptionHandler 작동 안됨 해결해야 할 과제 어떻게 프론트에게 에러임을 알려줄 수 있는가
     */
/*    1. 엑세스 토큰 만료되었을 때 리프레시 토큰 db에 있으면 엑세스 토큰 재발급
      2, 엑세스 토큰 만료되었을 때 리프레시 토큰 db에 있는거 확인 후 만료일자가 지났다면 리프레시 토큰 삭제
       -> 엑세스 토큰 만료되고, 리프레시 토큰도 없기 때문에 "새로 로그인 하시오"라는 문구가 띄어져야 한다.
      3.  디폴트는 헤더에 저장된 토큰을 꺼내 유효한지 확인 (user, role, exp 등 확인하여 엔드포인트 role 별로 접근 가능)*/

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        //Authorization 이름을 가진 헤더의 값을 꺼내옴
        final String authHeader = request.getHeader("Authorization");
        String jwt;

        //authHeader가 null이고, Bearer로 시작하지 않다면 체인 내의 다음 필터를 호출
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            //체인 내의 다음 필터를 호출
            filterChain.doFilter(request, response);
            return;
        }

        // authHeader의 `Bearer `를 제외한 문자열 jwt에 담은
        jwt = authHeader.substring(7);


        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //accessToken이 만료되었다면
            if (jwtService.isTokenExpired(jwt)) {
                //쿠키의 refreshToken과 db에 저장된 refreshToken의 만료일을 확인하고 accessToken 재발급 / 만료되면 재로그인 exception
                handleExpiredToken(request, response);
                //accessToken이 만료되지 않았다면 유효한지 검증
            } else {
                authenticateUser(jwt, request);
            }
        }
        //체인 내의 다음 필터를 호출
        filterChain.doFilter(request, response);
    }

    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshTokenValue = extractRefreshTokenFromCookie(request);
        try {
            if (refreshTokenValue != null) {
                RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenValue);
                if (refreshToken != null) {
                    User user = refreshToken.getUser();
                    String accessToken = jwtService.generateAccessToken(user);
                    response.setHeader("Authorization", "Bearer " + accessToken);
                    return;
                }
            }
        } catch (Exception e) {
            throw new UserUnAuthorizedException("refreshToken이 만료되었습니다. 재로그인을 해주세요.");
        }
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        // 쿠키에서 refreshToken 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void authenticateUser(String jwt, HttpServletRequest request) {
        // jwt의 사용자 이름 추출
        String id = jwtService.extractUsername(jwt);

        //UserDetailsService에서 loadUserByUsername 메서드로 사용자 세부 정보 검색
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(id);
        } catch (Exception  e) {
            throw new UserUnAuthorizedException("유저를 찾을 수 없습니다.");
        }
        if (jwtService.isTokenValid(jwt, userDetails)) {
            //UsernamePasswordAuthenticationToken 대상을 생성 (사용자이름,암호(=null로 설정),권한)
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            //authenticationToken의 세부정보 설정
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //해당 인증 객체를 SecurityContextHolder에 authenticationToken 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
package place.skillexchange.backend.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import place.skillexchange.backend.auth.services.AuthServiceImpl;
import place.skillexchange.backend.auth.services.JwtService;
import place.skillexchange.backend.auth.services.RefreshTokenService;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.RefreshToken;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.UserUnAuthorizedException;
import place.skillexchange.backend.service.MailService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/")
@Slf4j
public class UserController {

    private final AuthServiceImpl authService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;


    /**
     * 회원가입
     */
    @PostMapping("/signUp")
    public UserDto.RegisterResponse register(@Validated @RequestBody UserDto.RegisterRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException, MessagingException {

        //DB에 회원 id, email, password 저장
        User user = authService.register(dto,bindingResult);
        String activeToken = jwtService.generateActiveToken(user);
        //active Token (계정 활성화 토큰) 발급
        mailService.getEmail(dto.getEmail(), dto.getId(), activeToken);

        return new UserDto.RegisterResponse(new UserDto.RegisterResponseDto(user), new UserDto.ResponseBasic(200, "회원가입 완료"));
    }

    /**
     * active Token (계정 활성화 토큰) 검증
     */
    @PostMapping("/activation")
    public UserDto.ResponseBasic activation(@RequestBody Map<String, String> requestBody) {
        String activeToken = requestBody.get("activeToken");
        String id = jwtService.extractUsername(activeToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(id);
        // 여기서 activeToken을 검증하고 처리하는 로직을 추가
        //isTokenValid가 false일때 토큰 만료 exception이 출려되어야 함 !!!
        if (!jwtService.isActiveTokenValid(activeToken, userDetails)) {
            // 토큰이 유효하지 않은 경우 예외를 발생시킴
            throw new UserUnAuthorizedException("토큰이 만료되었습니다");
        }

        // active 0->1 로 변경 (active가 1이여야 로그인 가능)
        authService.updateUserActiveStatus(id);

        return new UserDto.ResponseBasic(200, "계정이 활성화 되었습니다.");
    }

    /**
     * 사용자 로그인
     */
    @PostMapping("/signIn")
    public ResponseEntity<UserDto.RegisterResponseDto> login(@RequestBody UserDto.LoginResponseDto dto) {
        return authService.login(dto);
    }

    /**
     * 토큰 받고 검증 후 유저 id 반환
     */
    @GetMapping("/findId")
    public String getUserIdFromToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Authorization 이름을 가진 헤더의 값을 꺼내옴
        String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader????????????"+authHeader);
        String jwt = authHeader.substring(7);

        if (jwt != null) {
            //accessToken이 만료되었다면
            if (jwtService.isTokenExpired(jwt)) {
                //쿠키의 refreshToken과 db에 저장된 refreshToken의 만료일을 확인하고 accessToken 재발급 / 만료되면 재로그인 exception
                handleExpiredToken(request, response);
            } else {
                //accessToken이 만료되지 않았다면 유효한지 검증
                return authenticateUser(jwt, request);
            }
        }
        // 처리되지 않은 경우 예외를 던집니다.
        throw new UserUnAuthorizedException("사용자 인증에 실패하였습니다.");
    }

    public void handleExpiredToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshTokenValue = extractRefreshTokenFromCookie(request);
        if (refreshTokenValue != null) {
            RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenValue);
            if (refreshToken != null) {
                User user = refreshToken.getUser();
                String accessToken = jwtService.generateAccessToken(user);
                response.setHeader("Authorization", "Bearer " + accessToken);
            }
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

    private String authenticateUser(String jwt, HttpServletRequest request) {
        // jwt의 사용자 이름 추출
        String id = jwtService.extractUsername(jwt);

        //UserDetailsService에서 loadUserByUsername 메서드로 사용자 세부 정보 검색
        UserDetails userDetails = userDetailsService.loadUserByUsername(id);
        if (jwtService.isAccessTokenValid(jwt, userDetails)) {
            return id;
        }
        // 처리되지 않은 경우 예외를 던집니다.
        throw new UserUnAuthorizedException("사용자 인증에 실패하였습니다.");
    }

}

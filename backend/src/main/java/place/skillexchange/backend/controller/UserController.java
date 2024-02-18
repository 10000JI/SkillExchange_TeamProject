package place.skillexchange.backend.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import place.skillexchange.backend.auth.services.AuthServiceImpl;
import place.skillexchange.backend.auth.services.JwtService;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.UserUnAuthorizedException;
import place.skillexchange.backend.service.MailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/")
public class UserController {

    private final AuthServiceImpl authService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final UserDetailsService userDetailsService;

    /**
     * 회원가입
     */
    @PostMapping("/signUp")
    public UserDto.RegisterResponse register(@Validated @RequestBody UserDto.RegisterRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException, MessagingException {

        //DB에 회원 id, email, password 저장
        User user = authService.register(dto,bindingResult);
        //active Token (계정 활성화 토큰) 발급
        mailService.getEmail(dto.getEmail(), dto.getId());

        return new UserDto.RegisterResponse(new UserDto.RegisterResponseDto(user), new UserDto.ResponseBasic(200, "회원가입 완료"),jwtService.generateActiveToken(user));
    }

    /**
     * active Token (계정 활성화 토큰) 검증
     */
    @PostMapping("/activation")
    public UserDto.ResponseBasic activation(HttpServletRequest request) {
        // HttpServletRequest 객체를 사용하여 쿠키 배열을 가져옴
        Cookie[] cookies = request.getCookies();
        String activeToken = null;
        if (cookies != null) {
            // 쿠키 배열을 순회하면서 activeToken 쿠키를 찾음
            for (Cookie cookie : cookies) {
                if ("activeToken".equals(cookie.getName())) { // activeToken 쿠키를 찾음
                    activeToken = cookie.getValue(); // activeToken 값을 가져옴
                    String username = jwtService.extractUsername(activeToken);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    // 여기서 activeToken을 검증하고 처리하는 로직을 추가
                    if (jwtService.isTokenValid(activeToken, userDetails)) {
                        // active 0->1 로 변경 (active가 1이여야 로그인 가능)
                        authService.updateUserActiveStatus(username);
                        break;
                    }
                }
            }
        } else {
            throw new UserUnAuthorizedException("토큰이 만료되었습니다");
        }

        return new UserDto.ResponseBasic(200, "계정이 활성화 되었습니다.");
    }
}

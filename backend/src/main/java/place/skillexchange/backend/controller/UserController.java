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
import org.springframework.web.bind.annotation.*;
import place.skillexchange.backend.auth.services.AuthServiceImpl;
import place.skillexchange.backend.auth.services.JwtService;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.UserUnAuthorizedException;
import place.skillexchange.backend.service.MailService;

import java.util.Map;

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
        if (!jwtService.isTokenValid(activeToken, userDetails)) {
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
    @GetMapping("/signIn")
    public ResponseEntity<UserDto.RegisterResponseDto> login(@RequestBody UserDto.LoginResponseDto dto) {
        return authService.login(dto);
    }

}

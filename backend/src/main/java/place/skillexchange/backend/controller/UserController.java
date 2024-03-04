package place.skillexchange.backend.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.auth.services.AuthService;
import place.skillexchange.backend.auth.services.AuthServiceImpl;
import place.skillexchange.backend.auth.services.JwtService;
import place.skillexchange.backend.auth.services.RefreshTokenService;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.RefreshToken;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.UserUnAuthorizedException;
import place.skillexchange.backend.repository.UserRepository;
import place.skillexchange.backend.service.MailService;
import place.skillexchange.backend.service.UserService;
import place.skillexchange.backend.service.UserServiceImpl;
import place.skillexchange.backend.util.SecurityUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/")
@Slf4j
public class UserController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signUp")
    public ResponseEntity<UserDto.SignUpInResponse> register(@Validated @RequestBody UserDto.SignUpRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException, MessagingException, IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto, bindingResult));
    }


    /**
     * active Token (계정 활성화 토큰) 검증
     */
    @PostMapping("/activation")
    public UserDto.ResponseBasic activation(@RequestBody Map<String, String> requestBody) {
        return authService.activation(requestBody);
    }

    /**
     * 사용자 로그인
     */
    @PostMapping("/signIn")
    public ResponseEntity<UserDto.SignUpInResponse> login(@RequestBody UserDto.SignInRequest dto) {
        return authService.login(dto);
    }

    /**
     * 아이디 찾기
     */
    @PostMapping("/emailToFindId")
    public UserDto.ResponseBasic emailToFindId(@RequestBody UserDto.EmailRequest dto) throws MessagingException, IOException {
        mailService.getEmailToFindId(dto.getEmail());
        return new UserDto.ResponseBasic(200, "이메일이 성공적으로 전송되었습니다.");
    }

    /**
     * 비밀번호 찾기
     */
    @PostMapping("/emailToFindPw")
    public UserDto.ResponseBasic emailToFindPw(@RequestBody UserDto.EmailRequest dto) throws MessagingException, IOException {
        mailService.getEmailToFindPw(dto.getEmail());
        return new UserDto.ResponseBasic(200, "이메일이 성공적으로 전송되었습니다.");
    }

    /**
     * 프로필 수정
     */
    @PatchMapping("/profileUpdate")
    public UserDto.ProfileResponse profileUpdate(@RequestPart("profileDto") UserDto.ProfileRequest dto, @RequestPart(value="imgFile", required = false) MultipartFile multipartFile) throws IOException {
        return userService.profileUpdate(dto, multipartFile);
    }

    /**
     * 프로필 조회
     */
    @GetMapping("/userInfo")
    public UserDto.MyProfileResponse profileRead() {
        return userService.profileRead();
    }

    /**
     * 비밀번호 변경
     */
    @PostMapping("/updatePw")
    public UserDto.ResponseBasic updatePw(@Validated @RequestBody UserDto.UpdatePwRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException {
        return userService.updatePw(dto, bindingResult);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/withdraw")
    public UserDto.ResponseBasic withdraw(HttpServletRequest request, HttpServletResponse response) {
        return authService.withdraw(request, response);
    }
}

package place.skillexchange.backend.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.auth.services.AuthServiceImpl;
import place.skillexchange.backend.auth.services.JwtService;
import place.skillexchange.backend.auth.services.RefreshTokenService;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.RefreshToken;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.UserUnAuthorizedException;
import place.skillexchange.backend.repository.UserRepository;
import place.skillexchange.backend.service.MailService;
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

    private final AuthServiceImpl authService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final SecurityUtil securityUtil;

    /**
     * 회원가입
     */
    @PostMapping("/signUp")
    public UserDto.SignUpInResponse register(@Validated @RequestBody UserDto.SignUpRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException, MessagingException, IOException {

        //DB에 회원 id, email, password 저장
        User user = authService.register(dto, bindingResult);
        //5분 뒤 회원의 active가 0이라면 db에서 회원 정보 삭제 (active 토큰 만료일에 맞춰서)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 5분 후에 실행될 작업
                System.out.println("1시간 후에 한 번 실행됩니다.");
                if (userRepository.findByIdAndActiveIsTrue(user.getId()) == null) {
                    userRepository.delete(user);
                }
                timer.cancel(); // 작업 완료 후 타이머 종료
            }
        }, 5 * 60 * 1000); // 5분 후 = 1시간 후에 작업 실행
        String activeToken = jwtService.generateActiveToken(user);
        //active Token (계정 활성화 토큰) 발급
        mailService.getEmail(dto.getEmail(), dto.getId(), activeToken);

        return new UserDto.SignUpInResponse(user, 200, "이메일(" + dto.getEmail() + ")을 확인하여 회원 활성화를 완료해주세요.");
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
    public ResponseEntity<UserDto.SignUpInResponse> login(@RequestBody UserDto.SignInRequest dto) {
        return authService.login(dto);
    }

//    /**
//     * 토큰 받고 검증 후 유저 id 반환
//     */
//    @GetMapping("/findId")
//    public ResponseEntity<String> getUserIdFromToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        // Authorization 헤더에서 토큰을 가져옴
//        String authHeader = request.getHeader("Authorization");
//        System.out.println("authHeader: " + authHeader);
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String jwt = authHeader.substring(7);
//
//            if (jwt != null) {
//                String id = authService.authenticateUser(jwt);
//                if (id != null) {
//                    // 헤더에 accessToken 추가
//                    response.setHeader("Authorization", "Bearer " + jwt);
//                    return ResponseEntity.ok(id);
//                } else {
//                    // 사용자 인증 실패일 때 예외를 던짐
//                    throw new UserUnAuthorizedException("사용자 인증에 실패하였습니다.");
//                }
//            }
//        }
//        // 토큰이 제공되지 않은 경우 예외를 던짐
//        throw new UserUnAuthorizedException("토큰이 제공되지 않았거나 유효하지 않습니다.");
//    }

//    /**
//     * 토큰 받고 검증 후 유저 id 반환
//     */
//    @GetMapping("/findId")
//    public ResponseEntity<String> getUserIdFromToken() throws IOException {
//        String id = securityUtil.getCurrentMemberUsername();
//        return ResponseEntity.ok(id);
//    }


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

//    /**
//     * 프로필 수정
//     */
//    @PatchMapping("/profileUpdate")
//    public UserDto.ProfileResponse profileUpdate(@RequestBody UserDto.ProfileRequest dto) throws IOException {
//        return userService.profileUpdate(dto);
//    }
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

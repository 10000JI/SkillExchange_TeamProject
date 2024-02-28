package place.skillexchange.backend.auth.services;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.RefreshToken;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.exception.UserUnAuthorizedException;
import place.skillexchange.backend.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    /* 회원가입 ~ 로그인 까지 (JWT 생성) */

    /**
     * 회원가입
     */
    @Override
    public User register(UserDto.SignUpRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException {

        boolean isValid = validateDuplicateMember(dto, bindingResult);
        if (isValid) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        //user 저장
        return userRepository.save(dto.toEntity());
    }


    /**
     * 회원가입 검증
     */
    @Override
    @Transactional
    public boolean validateDuplicateMember(UserDto.SignUpRequest dto, BindingResult bindingResult) {

        boolean checked = false;
        //checked가 true면 검증 발견
        //checked가 false면 검증 미발견

        checked = bindingResult.hasErrors();

        //방법1. 동일 id와 email만 계속해서 접근 가능 , 동일 id나 email이 다르면 접근 불가능 / 동일 email이나 id가 다르면 접근 불가능 (유효성검사)
        //active가 0이고, id와 email이 db에 있는 경우엔 if문을 건너뛴다.
        Optional<User> userOptional = userRepository.findByEmailAndIdAndActiveIsFalse(dto.getEmail(), dto.getId());
        if (!userOptional.isPresent()) {
            //id가 db에 있는 경우 if문 실행
            if(userRepository.findById(dto.getId()) != null) {
                //id 중복 검증
                Optional<User> byId = userRepository.findById(dto.getId());
                if (!byId.isEmpty()) {
                    bindingResult.rejectValue("id", "user.id.notEqual");
                    checked = true;
                }
            }
            //email이 db에 있는 경우 if문 실행
            if(userRepository.findByEmail(dto.getEmail()) != null) {

                //email 중복 검증
                Optional<User> userEmail = userRepository.findByEmail(dto.getEmail());
                if (userEmail.isPresent()) {
                    bindingResult.rejectValue("email", "user.email.notEqual");
                    checked = true;
                }
            }
        }

        //password 일치 검증
        if (!dto.getPasswordCheck().equals(dto.getPassword())) {
            bindingResult.rejectValue("passwordCheck", "user.password.notEqual");
            checked = true;
        }

        return checked;
    }

    /**
     * active 컬럼 0->1 변경
     */
    @Transactional
    @Override
    public void updateUserActiveStatus(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.changeActive(true);
        //userRepository.save(user);
    }

    /**
     * 로그인
     */
    @Override
    public ResponseEntity<UserDto.SignUpInResponse> login(UserDto.SignInRequest dto) {
        //authenticationManager가 authenticate() = 인증한다.
        try {
            //authenticationManager가 authenticate() = 인증한다.
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getId(),
                            dto.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            // 잘못된 아이디 패스워드 입력으로 인한 예외 처리
            throw new UserUnAuthorizedException("잘못된 정보입니다. 다시 입력하세요.");
        }

        //유저의 아이디 및 계정활성화 유무를 가지고 유저 객체 조회
        User user = userRepository.findByIdAndActiveIsTrue(dto.getId());
        if (user == null) {
            throw new UsernameNotFoundException(String.format("ID[%s]를 찾을 수 없습니다.", dto.getId()));
        }

        //accessToken 생성
        String accessToken = jwtService.generateAccessToken(user);
        //refreshToken 생성
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(dto.getId());

        // 헤더에 access 토큰 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        //쿠키에 refresh 토큰 추가
        ResponseCookie responseCookie = ResponseCookie
                .from("refreshToken", refreshToken.getRefreshToken())
                .secure(true)
                .httpOnly(true)
                .path("/")
                .sameSite("None")
                .build();
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        // ResponseEntity에 헤더만 설정하여 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(new UserDto.SignUpInResponse(user, 200, "로그인 성공!"));
    }

//    /**
//     * 헤더의 엑세스 토큰 jwt 검증
//     */
//    @Override
//    public String authenticateUser(String jwt) {
//        // jwt의 사용자 이름 추출
//        String id = jwtService.extractUsername(jwt);
//
//        //UserDetailsService에서 loadUserByUsername 메서드로 사용자 세부 정보 검색
//        UserDetails userDetails = userDetailsService.loadUserByUsername(id);
//        if (jwtService.isAccessTokenValid(jwt, userDetails)) {
//            return id;
//        }
//        // 처리되지 않은 경우 예외를 던진다.
////        throw new UserUnAuthorizedException("사용자 인증에 실패하였습니다.");
//        return null;
//    }

    @Override
    public UserDto.ResponseBasic withdraw(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}

package place.skillexchange.backend.auth.services;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.RefreshToken;
import place.skillexchange.backend.entity.User;
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

    /**
     * 사용자 등록
     */
    @Override
    public User register(UserDto.RegisterRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException {

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
    public boolean validateDuplicateMember(UserDto.RegisterRequest dto, BindingResult bindingResult) {

        boolean checked = false;
        //checked가 true면 검증 발견
        //checked가 false면 검증 미발견

        checked = bindingResult.hasErrors();

        //id 중복 검증
        Optional<User> byId = userRepository.findById(dto.getId());
        if (!byId.isEmpty()) {
            bindingResult.rejectValue("id", "user.id.notEqual");
            checked = true;
        }

        //password 일치 검증
        if (!dto.getPasswordCheck().equals(dto.getPassword())) {
            bindingResult.rejectValue("passwordCheck", "user.password.notEqual");
            checked = true;
        }

        //email 중복 검증
        Optional<User> userEmail = userRepository.findByEmail(dto.getEmail());
        if (userEmail.isPresent()) {
            bindingResult.rejectValue("email", "user.email.notEqual");
            checked = true;
        }

        return checked;
    }

    /**
     * active 컬럼 0->1 변경
     */
    @Override
    public void updateUserActiveStatus(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.changeActive(true);
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<UserDto.RegisterResponseDto> login(UserDto.LoginResponseDto dto) {
        //authenticationManager가 authenticate() = 인증한다.
        //이미 AuthFilterService에서 유효한 jwt인지 인증하는 작업을 로그인 인증 전에 시행
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getId(),
                        dto.getPassword()
                )
        );

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

        // 쿠키에 refreshToken 추가
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + refreshToken.getRefreshToken() + "; HttpOnly; Path=/");


        // ResponseEntity에 헤더만 설정하여 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(new UserDto.RegisterResponseDto(user));
    }
}

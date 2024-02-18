package place.skillexchange.backend.auth.services;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
  //  private final RefreshTokenService refreshTokenService;
  //  private final AuthenticationManager authenticationManager;

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

    @Override
    public void updateUserActiveStatus(String id) {
        User user = userRepository.findById(id).orElseThrow();
        user.changeActive(true);
    }
}

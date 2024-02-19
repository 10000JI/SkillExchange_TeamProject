package place.skillexchange.backend.auth.services;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.User;

public interface AuthService {

    public User register(UserDto.RegisterRequest dto, BindingResult bindingResult)  throws MethodArgumentNotValidException;

    public boolean validateDuplicateMember(UserDto.RegisterRequest dto, BindingResult bindingResult);

    void updateUserActiveStatus(String id);

    public ResponseEntity<UserDto.RegisterResponseDto> login(UserDto.LoginResponseDto dto);
}

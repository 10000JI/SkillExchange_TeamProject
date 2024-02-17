package place.skillexchange.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import place.skillexchange.backend.auth.services.AuthServiceImpl;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/")
public class UserController {

    private final AuthServiceImpl authService;

    @PostMapping("/signUp")
    public UserDto.Response register(@Validated @RequestBody UserDto.RegisterRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException {

        User user = authService.register(dto,bindingResult);

        return new UserDto.Response(new UserDto.RegisterResponseDto(user), 200, "회원가입 완료");
    }
}

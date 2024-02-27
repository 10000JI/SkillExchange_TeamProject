package place.skillexchange.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import place.skillexchange.backend.dto.UserDto;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.repository.UserRepository;
import place.skillexchange.backend.util.SecurityUtil;


import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto.ProfileResponse profileUpdate(UserDto.ProfileRequest dto) throws IOException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));
        user.changeProfileField(dto);

        return new UserDto.ProfileResponse(user, 200, "프로필이 성공적으로 변경되었습니다.");
    }

    @Override
    public UserDto.MyProfileResponse profileRead() {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));

        return new UserDto.MyProfileResponse(user, 200, id+"님의 프로필");
    }

    @Override
    @Transactional
    public UserDto.ResponseBasic updatePw(UserDto.UpdatePwRequest dto, BindingResult bindingResult) throws MethodArgumentNotValidException {
        String id = securityUtil.getCurrentMemberUsername();
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾지 못했습니다 : " + id));

        boolean checked = false;

        checked = bindingResult.hasErrors();

        //계정의 비밀번호와 내가 입력한 현재 비밀번호와 동일한지 유효성 검사
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            bindingResult.rejectValue("password","user.nowPassword.notEqual");
            checked = true;
        }


        //password 일치 검증
        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            bindingResult.rejectValue("newPasswordCheck", "user.newPassword.notEqual");
            checked = true;
        }

        // checked가 true면 유효섬 검사 실패, 에러핸들링 동작
        if (checked) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        // 새비밀번호로 변경 (트랜잭션으로 영속성 컨텍스트 속성 이용)
        user.changePw(passwordEncoder.encode(dto.getNewPassword()));

        return new UserDto.ResponseBasic(200, id+"님의 비밀번호가 변경되었습니다.");
    }
}

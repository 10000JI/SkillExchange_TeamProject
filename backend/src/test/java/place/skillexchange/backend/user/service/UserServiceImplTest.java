package place.skillexchange.backend.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.file.entity.File;
import place.skillexchange.backend.file.service.FileServiceImpl;
import place.skillexchange.backend.user.dto.UserDto;
import place.skillexchange.backend.user.entity.User;
import place.skillexchange.backend.user.repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private FileServiceImpl fileHandler;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    @DisplayName("프로필 수정 테스트")
    public void testProfileUpdate() throws IOException {
        // given
        UserDto.ProfileRequest profileRequest = UserDto.ProfileRequest.builder()
                .gender("femail")
                .careerSkills("작년까지 회사 다니다가 프리랜서로 전향한 백엔드 개발자입니다")
                .preferredSubject("베이스 기타")
                .mySubject("React, Spring Boot, Java, JavaScript.. 등 보유").build();// 필요한 프로파일 요청 정보 생성
        MultipartFile multipartFile = mock(MultipartFile.class); // MultipartFile 모킹

        Authentication authentication = new TestingAuthenticationToken("sksk436", "12345qwerQWER!", "ROLE_USER");
        //해당 인증 객체를 SecurityContextHolder에 authenticationToken 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // userRepository.findById() 메서드 모킹
        given(userRepository.findById(authentication.getPrincipal().toString())).willReturn(Optional.of(mock(User.class)));

        // fileHandler.uploadFilePR() 메서드의 반환값 설정
        given(fileHandler.uploadFilePR(any(MultipartFile.class), any(User.class))).willReturn(mock(File.class));

        // when
        UserDto.ProfileResponse result = userService.profileUpdate(profileRequest, multipartFile);

        // then
        assertThat(result.getReturnMessage()).isEqualTo("프로필이 성공적으로 변경되었습니다.");
    }

    @Test
    @DisplayName("프로필 조회 테스트")
    public void testProfileRead_Success() {
        // Given
        Authentication authentication = new TestingAuthenticationToken("sksk436", "12345qwerQWER!", "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = User.builder().id(authentication.getPrincipal().toString()).build();
        given(userRepository.findById(authentication.getPrincipal().toString()))
                .willReturn(Optional.of(user));

        // When
        UserDto.MyProfileResponse dto = userService.profileRead();

        // Then
        assertThat(dto.getId()).isEqualTo(authentication.getPrincipal().toString());
    }

    @Test
    @DisplayName("현재 비밀번호가 일치하지 않는 경우")
    public void testUpdatePassword_NewPasswordNotMatch() {
        // Given
        UserDto.UpdatePwRequest request = new UserDto.UpdatePwRequest("wrongPassword", "newPassword", "newPassword");
        BindingResult bindingResult = mock(BindingResult.class);

        Authentication authentication = new TestingAuthenticationToken("sksk436", "wrongPassword", "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = User.builder().id(authentication.getPrincipal().toString()).password("encodedPassword").build();
        given(userRepository.findById(authentication.getPrincipal().toString()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(false);

        // When
        Throwable thrown = catchThrowable(() -> userService.updatePw(request, bindingResult));

        // Then
        assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
        verify(bindingResult).rejectValue("password", "user.nowPassword.notEqual");
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    public void testUpdatePassword_Success() throws MethodArgumentNotValidException {
        // Given
        String currentPassword = "currentPassword";
        String newPassword = "newPassword";
        String encodedNewPassword = passwordEncoder.encode(newPassword);// 새로운 비밀번호 해시화

        UserDto.UpdatePwRequest request = new UserDto.UpdatePwRequest(currentPassword, newPassword, newPassword);
        BindingResult bindingResult = mock(BindingResult.class);

        Authentication authentication = new TestingAuthenticationToken("sksk436", "currentPassword", "ROLE_USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = User.builder().id(authentication.getPrincipal().toString()).password(passwordEncoder.encode(currentPassword)).build();
        given(userRepository.findById(authentication.getPrincipal().toString()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.getPassword(), user.getPassword())).willReturn(true);

        // When
        UserDto.ResponseBasic response = userService.updatePw(request, bindingResult);

        // Then
        assertThat(response.getReturnCode()).isEqualTo(200);
        assertThat(response.getReturnMessage()).isEqualTo(authentication.getPrincipal().toString() + "님의 비밀번호가 변경되었습니다.");
        // 변경된 비밀번호 확인
        assertThat(user.getPassword()).isEqualTo(encodedNewPassword);
    }
}
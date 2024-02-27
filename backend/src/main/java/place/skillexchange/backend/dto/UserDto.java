package place.skillexchange.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import place.skillexchange.backend.entity.Authority;
import place.skillexchange.backend.entity.File;
import place.skillexchange.backend.entity.User;
import place.skillexchange.backend.file.UploadFile;

import java.util.Collections;

public class UserDto {

    /**
     * 회원가입 시 요청된 Dto
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SignUpRequest {

        @NotBlank(message = "아이디: 필수 정보입니다.")
        @Size(min = 5 , message="id는 5글자 이상 입력해 주세요.")
        private String id;

        @NotBlank(message = "이메일: 필수 정보입니다.")
        @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
        private String email;

        @NotBlank(message = "비밀번호: 필수 정보입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        @NotBlank(message = "비밀번호 확인: 필수 정보입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String passwordCheck;

        //Authority 객체를 생성하고, 권한 이름을 "ROLE_USER"로 설정
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        /* Dto -> Entity */
        //toEntity는 패스워드 확인 일치하면 사용
        public User toEntity() {
            User user = User.builder()
                    .id(id)
                    .email(email)
                    .password(password)
                    .authorities(Collections.singleton(authority))
                    .build();
            return user;
        }
    }

    /**
     * 로그인 성공시 요청된 Dto
     */
    @Getter
    public static class SignInRequest {
        private String id;
        private String password;
    }

    /**
     * 회원가입, 로그인 성공시 보낼 Dto
     */
    @Getter
    public static class SignUpInResponse {
        private String id;
        private String email;
        private int returnCode;
        private String returnMessage;

        /* Entity -> Dto */
        public SignUpInResponse(User user, int returnCode, String returnMessage) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.returnCode = returnCode;
            this.returnMessage = returnMessage;
        }
    }
    /**
     * 응답코드, 응답메세지
     */
    @Getter
    @AllArgsConstructor
    public static class ResponseBasic {
        private int returnCode;
        private String returnMessage;
    }

    /**
     * 로그인 성공시 요청된 Dto
     */
    @Getter
    public static class EmailRequest {
        private String email;
    }

    /**
     * 프로필 수정 시 요청된 Dto
     */
    @Getter
    public static class ProfileRequest {
        private String gender;
        private String job;
        private String careerSkills;
        private String preferredSubject;
        private String mySubject;
    }

    /**
     * 프로필 수정 시 응답 Dto
     */
    @Getter
    public static class ProfileResponse {
        private String id;
        private String email;
        private String gender;
        private String job;
        private String careerSkills;
        private String preferredSubject;
        private String mySubject;
        private int returnCode;
        private String returnMessage;

        /* Entity -> Dto */
        public ProfileResponse(User user, int returnCode, String returnMessage) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.gender = user.getGender();
            this.job = user.getJob();
            this.careerSkills = user.getCareerSkills();
            this.preferredSubject = user.getPreferredSubject();
            this.mySubject = user.getMySubject();
            this.returnCode = returnCode;
            this.returnMessage = returnMessage;
        }
    }

    /**
     * 프로필 조회 시 응답 Dto
     */
    @Getter
    public static class MyProfileResponse {
        private String id;
        private String email;
        private String gender;
        private String job;
        private String careerSkills;
        private String preferredSubject;
        private String mySubject;
        private String imgUrl;
        private int returnCode;
        private String returnMessage;

        /* Entity -> Dto */
        public MyProfileResponse(User user, int returnCode, String returnMessage) {
            this.id = user.getId();
            this.email = user.getEmail();
            this.gender = user.getGender();
            this.job = user.getJob();
            this.careerSkills = user.getCareerSkills();
            this.preferredSubject = user.getPreferredSubject();
            this.mySubject = user.getMySubject();
            this.imgUrl = user.getFile().getFileUrl();
            this.returnCode = returnCode;
            this.returnMessage = returnMessage;
        }
    }

    /**
     * 비밀번호 변경 시 요청된 Dto
     */
    @Getter
    public static class UpdatePwRequest {
        @NotBlank(message = "현재 비밀번호: 필수 정보입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        @NotBlank(message = "새 비밀번호: 필수 정보입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String newPassword;

        @NotBlank(message = "새 비밀번호 확인: 필수 정보입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String newPasswordCheck;
    }
}

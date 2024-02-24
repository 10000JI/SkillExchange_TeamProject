package place.skillexchange.backend.file;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+";
    private static final SecureRandom random = new SecureRandom();

    public String generatePassword() {
        StringBuilder password = new StringBuilder();
        int length = random.nextInt(9) + 8; // 8~16자 길이의 비밀번호 생성

        for (int i = 0; i < length; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }
}

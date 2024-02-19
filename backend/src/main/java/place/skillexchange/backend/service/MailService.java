package place.skillexchange.backend.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import place.skillexchange.backend.file.MailManager;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MailManager mailManager;
    private final TemplateEngine templateEngine;

    public void getEmail(String email, String id, String activeToken) throws MessagingException {
        // Thymeleaf를 사용하여 메일 템플릿 작성
        Context context = new Context();
        context.setVariable("id",id);
        context.setVariable("jwtLink", "http://localhost:3000/active/"+activeToken);
        String emailContent = templateEngine.process("email-template", context);

        // 메일 발송
        String subject = "재능교환소 계정 활성화를 위한 로그인 인증";
        mailManager.send(email, subject, emailContent);
    }
}

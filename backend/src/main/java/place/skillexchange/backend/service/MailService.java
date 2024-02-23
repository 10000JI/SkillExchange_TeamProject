package place.skillexchange.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;


    public void getEmail(String email, String id, String activeToken) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        //메일 제목 설정
        helper.setSubject("재능교환소 계정 활성화를 위한 로그인 인증");

        //수신자 설정
        helper.setTo(email);

        //송신자 설정
        helper.setFrom(sender);

        //템플릿에 전달할 데이터 설정
        HashMap<String, String> emailValues = new HashMap<>();
        emailValues.put("id", id);
        emailValues.put("jwtLink", "http://localhost:3000/active/"+activeToken);

        Context context = new Context();
        emailValues.forEach((key, value)->{
            context.setVariable(key, value);
        });

        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process("email-template", context);
        helper.setText(html, true);

        //템플릿에 들어가는 이미지 cid로 삽입
        helper.addInline("image", new ClassPathResource("static/img/Logo.png"));

        //메일 보내기
        emailSender.send(message);
    }
}

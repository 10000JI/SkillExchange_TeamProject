package place.skillexchange.backend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ExceptionResponse.SmallDetails exceptionResponse = new ExceptionResponse.SmallDetails(
                "계정에 다시 로그인 해야 합니다.",
                request.getRequestURI());

        ResponseEntity<ExceptionResponse.SmallDetails> entity = ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) // HTTP 상태 코드 401 Forbidden 설정
                .body(exceptionResponse); // 응답 본문에 객체를 넣어 JSON으로 변환하여 전달

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // HTTP 상태 코드 설정
        response.setContentType("application/json"); // JSON 형태의 응답 설정
        response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정
        response.getWriter().write(new ObjectMapper().writeValueAsString(entity.getBody())); // 응답 내용 출력
    }
}

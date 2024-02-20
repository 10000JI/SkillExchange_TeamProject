package place.skillexchange.backend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.AccessDeniedException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ExceptionResponse.OneDetail exceptionResponse = new ExceptionResponse.OneDetail(
                new Date(),
                "접근이 거부되었습니다.",
                ((ServletWebRequest) request).getRequest().getRequestURI()); // 접근이 거부된 URI를 가져와서 추가

        ResponseEntity<ExceptionResponse.OneDetail> entity = ResponseEntity
                .status(HttpStatus.FORBIDDEN) // HTTP 상태 코드 403 Forbidden 설정
                .body(exceptionResponse); // 응답 본문에 객체를 넣어 JSON으로 변환하여 전달

        response.setStatus(HttpStatus.FORBIDDEN.value()); // HTTP 상태 코드 설정
        response.setContentType("application/json"); // JSON 형태의 응답 설정
        response.setCharacterEncoding("UTF-8"); // 문자 인코딩 설정
        response.getWriter().write(new ObjectMapper().writeValueAsString(entity.getBody())); // 응답 내용 출력
    }
}

package place.skillexchange.backend.exception;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@ControllerAdvice //AOP
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public CustomizedResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class) //타 Controller 실행 중 Exception 에러 발생 시 handlerAllExceptions()가 작업 우회
    public final ResponseEntity<Object> handlerAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse.OneDetail exceptionResponse = new ExceptionResponse.OneDetail(new Date(), ex.getMessage(), request.getDescription(false));
        //request.getDescription(false) : 클라이언트에게 상세정보를 보여주지 않을 것
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR); //반환은 ResponseEntity
    }

    /**
     * User를 찾을 수 없음
     */
    @ExceptionHandler(UsernameNotFoundException.class) //타 Controller 실행 중 UserNotFoundException 에러 발생 시 (=사용자 정보가 존재하지 않았을 때) handlerUserNotException()가 작업 우회
    public final ResponseEntity<Object> handlerUserNotException(UsernameNotFoundException ex, WebRequest request) {
        ExceptionResponse.OneDetail exceptionResponse = new ExceptionResponse.OneDetail(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 비인증
     */
    @ExceptionHandler(UserUnAuthorizedException.class) //타 Controller 실행 중 UserUnAuthorizedException 에러 발생 시 (=인증 자격 증명이 유효하지 않은 경우) handlerUserUnAuthorizedException()가 작업 우회
    public final ResponseEntity<Object> handlerUserUnAuthorizedException(UserUnAuthorizedException ex, WebRequest request) {
        ExceptionResponse.OneDetail exceptionResponse = new ExceptionResponse.OneDetail(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 이메일 전송 시 오류
     */
    @ExceptionHandler(MessagingException.class) //타 Controller 실행 중 MessagingException 에러 발생 시 handlerInValidEmailException()가 작업 우회
    public final ResponseEntity<Object> handlerInValidEmailException(MessagingException ex, WebRequest request) {
        ExceptionResponse.OneDetail exceptionResponse = new ExceptionResponse.OneDetail(new Date(), "이메일 전송 중 문제가 발생하였습니다. 이메일이 유효한지 확인하세요.", request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 유효성 검사 실패
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        //ex.getBindingResult().toString(): details 필드는 MethodArgumentNotValidException 예외 객체의 getBindingResult의 문자열을 출력
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // 에러 메시지 목록
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            String errorMessage = messageSource.getMessage(fieldError, Locale.getDefault());
            if (errorMessage != null) {
                errorMessages.add(errorMessage);
            }
        }

        // ExceptionResponse 객체 생성
        ExceptionResponse.ManyDetails exceptionResponse = new ExceptionResponse.ManyDetails(new Date(), "유효성 검사 실패", errorMessages);


        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}

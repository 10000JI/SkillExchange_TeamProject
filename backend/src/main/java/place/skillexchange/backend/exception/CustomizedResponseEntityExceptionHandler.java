package place.skillexchange.backend.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    @ExceptionHandler(UserNotFoundException.class) //타 Controller 실행 중 UserNotFoundException 에러 발생 시 (=사용자 정보가 존재하지 않았을 때) handlerAllExceptions()가 작업 우회
    public final ResponseEntity<Object> handlerUserNotException(Exception ex, WebRequest request) {
        ExceptionResponse.OneDetail exceptionResponse = new ExceptionResponse.OneDetail(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //유효성 검사 실패 시 실행
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

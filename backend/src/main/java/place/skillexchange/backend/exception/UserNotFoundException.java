package place.skillexchange.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
//RunTimeException(500번) 예외 클래스 상속받아서 생성
public class UserNotFoundException extends RuntimeException{
    //받은 message를 부모클래스인 RunTimeException에 던짐
    public UserNotFoundException(String message) {
        super(message);
    }
}

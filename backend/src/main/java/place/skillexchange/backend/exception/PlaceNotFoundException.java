package place.skillexchange.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlaceNotFoundException extends RuntimeException {
    //받은 message를 부모클래스인 RunTimeException에 던짐
    public PlaceNotFoundException(String message) {
        super(message);
    }
}

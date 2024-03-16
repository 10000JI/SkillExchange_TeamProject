package place.skillexchange.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


//일반화된 예외 객체 생성
public class ExceptionResponse {


    private final boolean success = false;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OneDetail{
        private Date timeStamp;
        private String message;
        private String details;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ManyDetails{
        private final boolean success = false;
        private int status;
        private String message;
        private List<String> details;
        private Date timeStamp;
    }

}


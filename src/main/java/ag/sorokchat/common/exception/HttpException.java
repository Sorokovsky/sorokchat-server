package ag.sorokchat.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends RuntimeException {
    private final HttpStatus statusCode;

    public HttpException(String messageCode, HttpStatus statusCode) {
        this.statusCode = statusCode;
        super(messageCode);
    }

    public HttpException(String messageCode, HttpStatus statusCode, Throwable cause) {
        this.statusCode = statusCode;
        super(messageCode, cause);
    }
}

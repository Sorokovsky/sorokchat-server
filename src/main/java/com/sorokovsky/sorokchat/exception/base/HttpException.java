package com.sorokovsky.sorokchat.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String messageCode;

    public HttpException(HttpStatus status, String messageCode) {
        this.httpStatus = status;
        this.messageCode = messageCode;
        super(messageCode);
    }

    public HttpException(HttpStatus status, String messageCode, Throwable cause) {
        this.httpStatus = status;
        this.messageCode = messageCode;
        super(messageCode, cause);
    }
}

package com.sorokovsky.sorokchat.exception.http;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class HttpException extends RuntimeException {
    private final HttpStatus httpStatus;

    public HttpException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpException(String message, HttpStatus httpStatus, Throwable exception) {
        super(message, exception);
        this.httpStatus = httpStatus;
    }
}

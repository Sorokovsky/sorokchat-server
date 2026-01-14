package com.sorokovsky.sorokchat.exception.http;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends HttpException {

    public UnauthorizedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public UnauthorizedException(String message, HttpStatus httpStatus, Throwable exception) {
        super(message, httpStatus, exception);
    }
}

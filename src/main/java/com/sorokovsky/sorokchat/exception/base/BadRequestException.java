package com.sorokovsky.sorokchat.exception.base;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HttpException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(String messageCode) {
        super(STATUS, messageCode);
    }

    public BadRequestException(String messageCode, Throwable cause) {
        super(STATUS, messageCode, cause);
    }
}

package com.sorokovsky.sorokchat.exception.base;

import org.springframework.http.HttpStatus;

public class ConflictException extends HttpException {
    private static final HttpStatus STATUS = HttpStatus.CONFLICT;

    public ConflictException(String messageCode, Throwable cause) {
        super(STATUS, messageCode, cause);
    }

    public ConflictException(String messageCode) {
        super(STATUS, messageCode);
    }
}

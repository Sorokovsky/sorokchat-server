package com.sorokovsky.sorokchat.exception.base;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends HttpException {
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public ForbiddenException(String messageCode) {
        super(STATUS, messageCode);
    }

    public ForbiddenException(String messageCode, Throwable cause) {
        super(STATUS, messageCode, cause);
    }
}

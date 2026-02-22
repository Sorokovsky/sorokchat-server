package com.sorokovsky.sorokchat.exception.base;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpException {
    private static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    public NotFoundException(String messageCode) {
        super(STATUS, messageCode);
    }

    public NotFoundException(String messageCode, Throwable cause) {
        super(STATUS, messageCode, cause);
    }
}

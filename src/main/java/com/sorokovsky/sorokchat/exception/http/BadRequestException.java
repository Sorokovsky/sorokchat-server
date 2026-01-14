package com.sorokovsky.sorokchat.exception.http;

import org.springframework.http.HttpStatus;

public abstract class BadRequestException extends HttpException {
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    public BadRequestException(String message) {
        super(message, STATUS);
    }

    public BadRequestException(String message, Throwable exception) {
        super(message, STATUS, exception);
    }
}

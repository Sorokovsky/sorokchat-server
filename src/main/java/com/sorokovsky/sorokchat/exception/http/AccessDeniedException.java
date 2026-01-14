package com.sorokovsky.sorokchat.exception.http;

import org.springframework.http.HttpStatus;

public abstract class AccessDeniedException extends HttpException {
    private static final HttpStatus STATUS = HttpStatus.FORBIDDEN;

    public AccessDeniedException(String message) {
        super(message, STATUS);
    }

    public AccessDeniedException(String message, Throwable exception) {
        super(message, STATUS, exception);
    }
}

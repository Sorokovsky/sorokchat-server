package com.sorokovsky.sorokchat.exception.base;

import org.springframework.http.HttpStatus;

public class InternalServerException extends HttpException {
    private static final HttpStatus STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String MESSAGE_CODE = "errors.unknown";

    public InternalServerException() {
        super(STATUS, MESSAGE_CODE);
    }

    public InternalServerException(Throwable cause) {
        super(STATUS, MESSAGE_CODE, cause);
    }
}

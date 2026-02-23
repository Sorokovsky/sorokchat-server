package com.sorokovsky.sorokchat.exception.authorization;

import com.sorokovsky.sorokchat.exception.base.BadRequestException;

public class BadCredentialsException extends BadRequestException {
    private static final String MESSAGE_CODE = "errors.authorization.bad-credentials";

    public BadCredentialsException() {
        super(MESSAGE_CODE);
    }

    public BadCredentialsException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }
}

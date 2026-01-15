package com.sorokovsky.sorokchat.exception.user;

import com.sorokovsky.sorokchat.exception.http.BadRequestException;

public class BadCredentialsException extends BadRequestException {
    private static final String MESSAGE = "error.bad-credentials";

    public BadCredentialsException() {
        super(MESSAGE);
    }

    public BadCredentialsException(Throwable exception) {
        super(MESSAGE, exception);
    }
}

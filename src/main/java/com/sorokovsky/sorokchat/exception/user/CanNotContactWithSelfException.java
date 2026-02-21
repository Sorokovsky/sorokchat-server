package com.sorokovsky.sorokchat.exception.user;

import com.sorokovsky.sorokchat.exception.http.BadRequestException;

public class CanNotContactWithSelfException extends BadRequestException {
    private static final String MESSAGE = "error.user.self-contact";

    public CanNotContactWithSelfException() {
        super(MESSAGE);
    }

    public CanNotContactWithSelfException(Throwable exception) {
        super(MESSAGE, exception);
    }
}

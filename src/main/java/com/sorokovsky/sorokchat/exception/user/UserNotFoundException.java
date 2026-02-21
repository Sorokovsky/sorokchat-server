package com.sorokovsky.sorokchat.exception.user;

import com.sorokovsky.sorokchat.exception.http.BadRequestException;

public class UserNotFoundException extends BadRequestException {
    private static final String MESSAGE = "error.user.not-found";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    public UserNotFoundException(Throwable exception) {
        super(MESSAGE, exception);
    }
}

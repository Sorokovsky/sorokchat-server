package com.sorokovsky.sorokchat.exception.user;

import com.sorokovsky.sorokchat.exception.http.BadRequestException;

public class UserAlreadyExists extends BadRequestException {
    private static final String MESSAGE = "error.user.already.exists";

    public UserAlreadyExists() {
        super(MESSAGE);
    }

    public UserAlreadyExists(Throwable exception) {
        super(MESSAGE, exception);
    }
}

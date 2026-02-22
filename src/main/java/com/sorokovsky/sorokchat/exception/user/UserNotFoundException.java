package com.sorokovsky.sorokchat.exception.user;

import com.sorokovsky.sorokchat.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private static final String MESSAGE_CODE = "errors.user.not-found";

    public UserNotFoundException() {
        super(MESSAGE_CODE);
    }

    public UserNotFoundException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }
}

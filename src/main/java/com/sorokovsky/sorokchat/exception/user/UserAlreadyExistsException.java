package com.sorokovsky.sorokchat.exception.user;

import com.sorokovsky.sorokchat.exception.base.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    private static final String MESSAGE_CODE = "errors.user.exists";

    public UserAlreadyExistsException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public UserAlreadyExistsException() {
        super(MESSAGE_CODE);
    }
}

package com.sorokovsky.sorokchat.exception.group;

import com.sorokovsky.sorokchat.exception.base.ConflictException;

public class GroupAlreadyExistsException extends ConflictException {
    private static final String MESSAGE_CODE = "errors.groups.exists";

    public GroupAlreadyExistsException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public GroupAlreadyExistsException() {
        super(MESSAGE_CODE);
    }
}

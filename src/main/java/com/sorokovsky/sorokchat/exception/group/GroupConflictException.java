package com.sorokovsky.sorokchat.exception.group;

import com.sorokovsky.sorokchat.exception.base.ConflictException;

public class GroupConflictException extends ConflictException {
    private final static String MESSAGE_CODE = "errors.groups.conflict";

    public GroupConflictException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public GroupConflictException() {
        super(MESSAGE_CODE);
    }
}

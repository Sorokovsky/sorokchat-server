package com.sorokovsky.sorokchat.exception.group;

import com.sorokovsky.sorokchat.exception.base.BadRequestException;

public class CanNotAddSelfException extends BadRequestException {
    private static final String MESSAGE_CODE = "errors.groups.members.self";

    public CanNotAddSelfException() {
        super(MESSAGE_CODE);
    }

    public CanNotAddSelfException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }
}

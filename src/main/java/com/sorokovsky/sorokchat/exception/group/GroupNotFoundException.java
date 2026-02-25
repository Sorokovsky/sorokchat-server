package com.sorokovsky.sorokchat.exception.group;

import com.sorokovsky.sorokchat.exception.base.NotFoundException;

public class GroupNotFoundException extends NotFoundException {
    private static final String MESSAGE_CODE = "errors.groups.not-found";

    public GroupNotFoundException() {
        super(MESSAGE_CODE);
    }

    public GroupNotFoundException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }
}

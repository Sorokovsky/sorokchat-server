package com.sorokovsky.sorokchat.exception.chat;

import com.sorokovsky.sorokchat.exception.base.ForbiddenException;

public class YouAreNotInChatException extends ForbiddenException {
    private static final String MESSAGE_CODE = "errors.chat.not-exists";

    public YouAreNotInChatException() {
        super(MESSAGE_CODE);
    }

    public YouAreNotInChatException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }
}

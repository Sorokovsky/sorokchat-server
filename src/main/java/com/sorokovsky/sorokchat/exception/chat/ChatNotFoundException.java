package com.sorokovsky.sorokchat.exception.chat;

import com.sorokovsky.sorokchat.exception.http.BadRequestException;

public class ChatNotFoundException extends BadRequestException {
    private static final String MESSAGE = "error.chat.not-found";

    public ChatNotFoundException() {
        super(MESSAGE);
    }

    public ChatNotFoundException(Throwable exception) {
        super(MESSAGE, exception);
    }
}

package com.sorokovsky.sorokchat.exception.contact;

import com.sorokovsky.sorokchat.exception.base.NotFoundException;

public class ContactNotFoundException extends NotFoundException {
    private static final String MESSAGE_CODE = "errors.contacts.not-found";

    public ContactNotFoundException() {
        super(MESSAGE_CODE);
    }

    public ContactNotFoundException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }
}

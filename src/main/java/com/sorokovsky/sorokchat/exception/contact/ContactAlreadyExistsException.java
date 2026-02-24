package com.sorokovsky.sorokchat.exception.contact;

import com.sorokovsky.sorokchat.exception.base.ConflictException;

public class ContactAlreadyExistsException extends ConflictException {
    private static final String MESSAGE_CODE = "errors.contacts.exists";

    public ContactAlreadyExistsException(Throwable cause) {
        super(MESSAGE_CODE, cause);
    }

    public ContactAlreadyExistsException() {
        super(MESSAGE_CODE);
    }
}

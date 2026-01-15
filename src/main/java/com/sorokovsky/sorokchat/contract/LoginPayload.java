package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginPayload(
        @NotNull(message = "{error.email.null}")
        @Email(message = "{error.email.incorrect}")
        String email,

        @NotNull(message = "{error.password.null}")
        @Size(min = 8, max = 20, message = "{error.password.size}")
        String password
) {
}

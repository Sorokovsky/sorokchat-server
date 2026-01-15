package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewUserPayload(
        @NotNull(message = "{error.email.null}")
        @Email(message = "{error.email.incorrect}")
        String email,

        @NotNull(message = "{error.password.null}")
        @Size(min = 8, max = 20, message = "{error.password.size}")
        String password,

        @NotNull(message = "{error.firstName.null}")
        @NotBlank(message = "{error.firstName.empty}")
        String firstName,

        @NotNull(message = "{error.lastName.null}")
        @NotBlank(message = "{error.lastName.empty}")
        String lastName,

        @NotNull(message = "{error.middleName.null}")
        @NotBlank(message = "{error.middleName.empty}")
        String middleName
) {
}

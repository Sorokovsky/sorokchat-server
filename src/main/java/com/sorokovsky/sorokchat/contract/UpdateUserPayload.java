package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public record UpdateUserPayload(
        Optional<String> nickname,

        Optional<String> displayName,

        Optional<@Size(min = 6, max = 32, message = "{errors.user.password.size}") String> password,

        Optional<@Pattern(regexp = "^\\+?[1-9]\\d{6,14}$", message = "{errors.user.phone.invalid}") String> phoneNumber,

        Optional<@Email(message = "{errors.user.email.invalid}") String> email
) {
}

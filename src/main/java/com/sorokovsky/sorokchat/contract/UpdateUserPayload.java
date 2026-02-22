package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.*;

public record UpdateUserPayload(
        @NotBlank(message = "{errors.user.nickname.empty}")
        String nickname,

        @NotNull(message = "{errors.user.display-name.empty}")
        String displayName,

        @NotNull(message = "{errors.user.password.empty}")
        @Size(min = 6, max = 32, message = "{errors.user.password.size}")
        String password,

        @Pattern(
                regexp = "^\\+?[1-9]\\d{6,14}$",
                message = "{errors.user.phone.invalid}"
        )
        String phoneNumber,

        @Email(message = "{errors.user.email.invalid}")
        String email
) {
}

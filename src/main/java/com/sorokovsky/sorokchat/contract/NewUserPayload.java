package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewUserPayload(
        @NotNull(message = "{errors.user.nickname.null}")
        @NotBlank(message = "{errors.user.nickname.empty}")
        String nickname,

        @NotNull(message = "{errors.user.display-name.null}")
        @NotNull(message = "{errors.user.display-name.empty}")
        String displayName,

        @NotNull(message = "{errors.user.password.null}")
        @NotNull(message = "{errors.user.password.empty}")
        @Size(min = 6, max = 32, message = "{errors.user.password.size}")
        String password
) {
}

package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewMessagePayload(
        @NotNull(message = "{errors.message.text.null}")
        @NotEmpty(message = "{errors.message.text.empty}")
        String text,

        @NotNull(message = "{errors.message.signing.null}")
        @NotEmpty(message = "{errors.message.signing.empty}")
        String mac
) {
}

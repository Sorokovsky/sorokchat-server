package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewMessagePayload(
        @NotNull(message = "{error.message.text.null}")
        @NotBlank(message = "{error.message.text.null}")
        String text,
        @NotNull(message = "{error.message.mac.null}")
        @NotBlank(message = "{error.message.mac.null}")
        String mac
) {
}

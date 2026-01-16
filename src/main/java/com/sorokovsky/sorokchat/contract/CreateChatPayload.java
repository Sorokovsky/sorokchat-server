package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateChatPayload(
        @NotNull(message = "{error.chat.name.null}")
        @NotBlank(message = "{error.chat.name.empty}")
        String name,

        @NotNull(message = "{error.description.name.null}")
        @NotBlank(message = "{error.description.name.empty}")
        String description
) {
}

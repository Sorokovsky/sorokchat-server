package com.sorokovsky.sorokchat.contract;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Optional;

public record NewGroupPayload(
        @NotNull(message = "{errors.groups.nickname.null}")
        @NotEmpty(message = "{errors.groups.nickname.empty}")
        String nickname,

        @NotNull(message = "{errors.groups.displayName.null}")
        @NotEmpty(message = "{errors.groups.displayName.empty}")
        String displayName,

        Optional<String> description,

        @NotNull(message = "{errors.groups.members.null}")
        @Size(min = 1, message = "{errors.groups.members.length}")
        List<Long> addedMembers
) {
}

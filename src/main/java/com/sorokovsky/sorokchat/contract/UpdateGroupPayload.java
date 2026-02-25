package com.sorokovsky.sorokchat.contract;

import java.util.Optional;

public record UpdateGroupPayload(
        Optional<String> nickname,
        Optional<String> displayName,
        Optional<String> description
) {
}

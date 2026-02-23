package com.sorokovsky.sorokchat.contract;

import java.util.Date;
import java.util.Optional;

public record GetUserPayload(
        Long id,
        Date createdAt,
        Date updatedAt,
        String nickname,
        String displayName,
        Optional<String> email,
        Optional<String> phoneNumber
) {
}

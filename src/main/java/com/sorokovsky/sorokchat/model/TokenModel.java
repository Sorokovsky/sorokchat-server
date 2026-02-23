package com.sorokovsky.sorokchat.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TokenModel(
        UUID id,
        String subject,
        List<String> authorities,
        Instant createdAt,
        Instant expiresAt
) {
}

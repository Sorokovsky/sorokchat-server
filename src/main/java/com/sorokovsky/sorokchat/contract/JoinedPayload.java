package com.sorokovsky.sorokchat.contract;

public record JoinedPayload(
        String chatId,
        GetUserPayload user
) {
}

package com.sorokovsky.sorokchat.contract;

public record LeavedPayload(
        String chatId,
        GetUserPayload user
) {
}

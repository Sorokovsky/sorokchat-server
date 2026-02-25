package com.sorokovsky.sorokchat.contract;

import java.util.Date;

public record GetMessagePayload(
        Date createdAt,
        Date updatedAt,
        String text,
        String mac,
        GetUserPayload author,
        String chatId
) {
}

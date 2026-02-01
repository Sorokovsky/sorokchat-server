package com.sorokovsky.sorokchat.contract;

import java.util.Date;

public record MessagePayload(
        Date createdAt,
        Date updatedAt,
        String text,
        String mac,
        Long authorId
) {
}

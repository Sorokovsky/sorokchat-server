package com.sorokovsky.sorokchat.contract;

import java.util.Date;
import java.util.List;

public record GetChatPayload(
        long id,
        Date createdAt,
        Date updatedAt,
        String name,
        String description,
        List<GetUserPayload> members
) {
}

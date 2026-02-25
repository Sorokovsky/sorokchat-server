package com.sorokovsky.sorokchat.contract;

import java.util.Date;
import java.util.List;

public record GetGroupPayload(
        Long id,
        Date createdAt,
        Date updatedAt,
        String nickname,
        String displayName,
        String description,
        List<GetUserPayload> members
) {
}

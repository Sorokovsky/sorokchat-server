package com.sorokovsky.sorokchat.contract;

import java.util.Date;

public record GetContactPayload(
        Long id,
        Date createdAt,
        Date updatedAt,
        GetUserPayload firstUser,
        GetUserPayload secondUser
) {
}

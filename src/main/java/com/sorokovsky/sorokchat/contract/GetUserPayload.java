package com.sorokovsky.sorokchat.contract;

import java.util.Date;
import java.util.List;

public record GetUserPayload(
        long id,
        Date createdAt,
        Date updatedAt,
        String email,
        String firstName,
        String lastName,
        String middleName,
        List<String> roles
) {
}

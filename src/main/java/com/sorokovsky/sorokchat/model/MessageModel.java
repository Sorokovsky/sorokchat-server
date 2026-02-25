package com.sorokovsky.sorokchat.model;

import java.util.Date;

public record MessageModel(
        Date createdAt,
        Date updatedAt,
        String text,
        String mac,
        String chatId,
        UserModel author
) {
}

package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.MessagePayload;
import com.sorokovsky.sorokchat.contract.NewMessagePayload;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.service.ChatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller()
@RequiredArgsConstructor
public class MessagesController {
    private final ChatsService chatsService;

    @MessageMapping("/chat.send/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public MessagePayload sendMessage(
            @DestinationVariable long chatId,
            @Valid NewMessagePayload newMessagePayload,
            @AuthenticationPrincipal UserModel user
    ) {
        return chatsService.writeMessage(newMessagePayload, user, chatId);
    }
}

package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.GetMessagePayload;
import com.sorokovsky.sorokchat.contract.NewMessagePayload;
import com.sorokovsky.sorokchat.mapper.MessageMapper;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.service.ChatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MessagesController {
    private final MessageMapper mapper;
    private final ChatsService chatsService;
    private final MessageSource messageSource;

    @MessageMapping("/chat.send/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public GetMessagePayload sendMessage(
            @DestinationVariable String chatId,
            @Valid NewMessagePayload payload,
            SimpMessageHeaderAccessor accessor
    ) {
        final var user = (UserModel) Objects.requireNonNull(accessor.getSessionAttributes()).get("CURRENT_USER");
        return mapper.toGet(chatsService.writeMessage(user, chatId, payload));
    }
}

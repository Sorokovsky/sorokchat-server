package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.ExchangeContract;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ExchangeController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/start-exchange.send/{chatId}")
    public void startExchange(@DestinationVariable String chatId) {
        messagingTemplate.convertAndSend("/topic/start-exchange/%s".formatted(chatId));
    }

    @MessageMapping("/send-public-key.send/{nodeId}")
    @SendTo("/topic/send-public-key/{nodeId}")
    public ExchangeContract sendPublicKey(ExchangeContract exchangeContract) {
        return exchangeContract;
    }
}

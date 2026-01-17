package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.CreateChatPayload;
import com.sorokovsky.sorokchat.contract.GetChatPayload;
import com.sorokovsky.sorokchat.mapper.ChatMapper;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.service.ChatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("chats")
@RequiredArgsConstructor
public class ChatsController {
    private final ChatsService service;
    private final ChatMapper mapper;

    @PostMapping("create")
    public ResponseEntity<GetChatPayload> create(
            @RequestBody @Valid CreateChatPayload payload,
            @AuthenticationPrincipal UserModel user,
            UriComponentsBuilder uriBuilder
    ) {
        final var chat = service.create(payload, user);
        return ResponseEntity
                .created(uriBuilder.replacePath("/chats/{id}").build(Map.of("id", chat.getId())))
                .body(mapper.toGet(chat));
    }

    @GetMapping("by-me")
    public ResponseEntity<List<GetChatPayload>> getAllByUser(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(service
                .getAllByUser(user)
                .stream().map(mapper::toGet)
                .toList()
        );
    }

    @DeleteMapping("{id:\\d++}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

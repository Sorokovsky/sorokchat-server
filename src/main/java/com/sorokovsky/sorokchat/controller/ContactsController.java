package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.GetContactPayload;
import com.sorokovsky.sorokchat.exception.contact.ContactNotFoundException;
import com.sorokovsky.sorokchat.exception.user.UserNotFoundException;
import com.sorokovsky.sorokchat.mapper.ContactMapper;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.service.ContactsService;
import com.sorokovsky.sorokchat.service.UsersService;
import com.sorokovsky.sorokchat.utils.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("contacts")
@RequiredArgsConstructor
public class ContactsController {
    private final ContactsService service;
    private final ContactMapper mapper;
    private final UsersService usersService;

    @PostMapping("create/{nickname}")
    public ResponseEntity<Void> createContact(
            @AuthenticationPrincipal
            UserModel user,
            @PathVariable String nickname,
            UriComponentsBuilder uriBuilder
    ) {
        final var secondUser = usersService.getByNickname(nickname).orElseThrow(UserNotFoundException::new);
        service.createContact(user, secondUser);
        return ResponseEntity
                .created(uriBuilder
                        .replacePath("/contacts/{id}")
                        .build(Map.of("id", Nickname.asNickname(secondUser.getNickname()))))
                .build();
    }

    @GetMapping("my")
    public ResponseEntity<List<GetContactPayload>> getUserContacts(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(service.getContactsByUser(user.getId()).stream().map(mapper::toGet).toList());
    }

    @GetMapping("by-user/{term}")
    public ResponseEntity<GetContactPayload> getContact(@AuthenticationPrincipal UserModel user, @PathVariable String term) {
        return ResponseEntity
                .ok(service.getContactByNickname(user.getId(), term).map(mapper::toGet).orElseThrow(ContactNotFoundException::new));
    }
}

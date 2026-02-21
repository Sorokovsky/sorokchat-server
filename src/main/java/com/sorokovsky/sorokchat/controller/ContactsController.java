package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.GetUserPayload;
import com.sorokovsky.sorokchat.mapper.UserMapper;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("contacts")
@RequiredArgsConstructor
public class ContactsController {
    private final UserMapper mapper;
    private final UsersService service;

    @PostMapping("add-by-email/{email}")
    public ResponseEntity<GetUserPayload> addByEmail(@AuthenticationPrincipal UserModel user, @PathVariable String email) {
        return ResponseEntity.ok(mapper.toGet(service.addToContact(user, email), true));
    }

    @DeleteMapping("remove-by-id/{id:\\d+}")
    public ResponseEntity<GetUserPayload> removeById(@AuthenticationPrincipal UserModel user, @PathVariable long id) {
        return ResponseEntity.ok(mapper.toGet(service.removeFromContact(user, id), true));
    }
}

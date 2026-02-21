package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.GetUserPayload;
import com.sorokovsky.sorokchat.contract.LoginPayload;
import com.sorokovsky.sorokchat.contract.NewUserPayload;
import com.sorokovsky.sorokchat.mapper.UserMapper;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.service.AuthorizationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("authorization")
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService service;
    private final UserMapper mapper;

    @PostMapping("register")
    public ResponseEntity<GetUserPayload> register(
            @RequestBody @Valid NewUserPayload payload,
            HttpServletResponse response,
            UriComponentsBuilder uriBuilder
    ) {
        final var user = service.register(payload, response);
        return ResponseEntity
                .created(uriBuilder.replacePath("/authorization/profile").build().toUri())
                .body(mapper.toGet(user, true));
    }

    @PostMapping("login")
    public ResponseEntity<GetUserPayload> login(
            @RequestBody @Valid LoginPayload payload,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(mapper.toGet(service.login(payload, response), true));
    }

    @DeleteMapping("logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        service.logout(response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("profile")
    public ResponseEntity<GetUserPayload> getProfile(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(mapper.toGet(user, true));
    }
}

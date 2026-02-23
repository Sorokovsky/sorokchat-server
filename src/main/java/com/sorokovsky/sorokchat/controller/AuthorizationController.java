package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.AuthorizedPayload;
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
    public ResponseEntity<AuthorizedPayload> register(
            @Valid @RequestBody NewUserPayload payload,
            HttpServletResponse response,
            UriComponentsBuilder uriBuilder
    ) {
        return ResponseEntity
                .created(uriBuilder.replacePath("/authorization/profile").build().toUri())
                .body(service.register(payload, response));
    }

    @GetMapping("profile")
    public ResponseEntity<GetUserPayload> profile(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(mapper.toGet(user));
    }

    @PostMapping("login")
    public ResponseEntity<AuthorizedPayload> login(@Valid @RequestBody LoginPayload payload, HttpServletResponse response) {
        return ResponseEntity.ok(service.login(payload, response));
    }

    @DeleteMapping("logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        service.logout(response);
        return ResponseEntity.noContent().build();
    }
}

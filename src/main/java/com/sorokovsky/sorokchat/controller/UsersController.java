package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.GetUserPayload;
import com.sorokovsky.sorokchat.contract.UpdateUserPayload;
import com.sorokovsky.sorokchat.exception.user.UserNotFoundException;
import com.sorokovsky.sorokchat.mapper.UserMapper;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService service;
    private final UserMapper mapper;

    @GetMapping("{term}")
    public ResponseEntity<List<GetUserPayload>> findUsers(@PathVariable String term) {
        if (term.startsWith("@")) {
            if (term.length() == 1) throw new UserNotFoundException();
            final var resultTerm = "%" + term.replaceFirst("@", "") + "%";
            final var user = service.getByNicknamePartial(resultTerm).orElse(null);
            if (user == null) throw new UserNotFoundException();
            return ResponseEntity.ok(List.of(mapper.toGet(user)));
        } else {
            final var resultTerm = "%" + term + "%";
            return ResponseEntity.ok(service.getByDisplayNamePartial(resultTerm).stream().map(mapper::toGet).toList());
        }
    }

    @PatchMapping
    public ResponseEntity<GetUserPayload> updateUser(
            @AuthenticationPrincipal UserModel user,
            @RequestBody @Valid UpdateUserPayload payload
    ) {
        return ResponseEntity.ok(mapper.toGet(service.update(user.getId(), payload)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserModel user) {
        service.deleteById(user.getId());
        return ResponseEntity.noContent().build();
    }
}

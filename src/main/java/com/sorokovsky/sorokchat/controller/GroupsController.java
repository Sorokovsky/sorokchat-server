package com.sorokovsky.sorokchat.controller;

import com.sorokovsky.sorokchat.contract.GetGroupPayload;
import com.sorokovsky.sorokchat.contract.NewGroupPayload;
import com.sorokovsky.sorokchat.contract.UpdateGroupPayload;
import com.sorokovsky.sorokchat.exception.group.GroupNotFoundException;
import com.sorokovsky.sorokchat.mapper.GroupMapper;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.service.GroupsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("groups")
@RequiredArgsConstructor
public class GroupsController {
    private final GroupsService service;
    private final GroupMapper mapper;

    @PostMapping
    public ResponseEntity<GetGroupPayload> createGroup(
            @AuthenticationPrincipal UserModel user,
            @Valid @RequestBody NewGroupPayload payload,
            UriComponentsBuilder uriBuilder
    ) {
        final var createdGroup = service.createGroup(user, payload);
        final var uri = uriBuilder.replacePath("/groups/by-id/{id}").build(Map.of("id", createdGroup.getId()));
        return ResponseEntity
                .created(uri)
                .body(mapper.toGet(createdGroup));
    }

    @GetMapping("my")
    public ResponseEntity<List<GetGroupPayload>> getMyGroups(@AuthenticationPrincipal UserModel user) {
        return ResponseEntity.ok(service.getMyGroups(user.getId()).stream().map(mapper::toGet).toList());
    }

    @GetMapping("by-term/{term}")
    public ResponseEntity<List<GetGroupPayload>> getGroupsByTerm(
            @PathVariable String term) {
        return ResponseEntity.ok(service.getGroupsByTerm(term).stream().map(mapper::toGet).toList());
    }

    @GetMapping("by-id/{id:\\d+}")
    public ResponseEntity<GetGroupPayload> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toGet(service.getById(id).orElseThrow(GroupNotFoundException::new)));
    }

    @PatchMapping("by-id/{id:\\d+}")
    public ResponseEntity<GetGroupPayload> updateGroup(@PathVariable Long id, @Valid @RequestBody UpdateGroupPayload payload) {
        return ResponseEntity.ok(mapper.toGet(service.updateGroup(id, payload)));
    }

    @DeleteMapping("by-id/{id:\\d+}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

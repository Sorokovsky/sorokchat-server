package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.CreateChatPayload;
import com.sorokovsky.sorokchat.exception.chat.ChatNotFoundException;
import com.sorokovsky.sorokchat.mapper.ChatMapper;
import com.sorokovsky.sorokchat.model.ChatModel;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.repository.ChatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatsService {
    private final ChatsRepository repository;
    private final ChatMapper mapper;

    @Transactional(readOnly = true)
    public Optional<ChatModel> getById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Transactional(readOnly = true)
    public Optional<ChatModel> getByName(String name) {
        return repository.findByName(name).map(mapper::toModel);
    }

    @Transactional(readOnly = true)
    public List<ChatModel> getAllByUser(UserModel user) {
        return repository
                .findAllByMembersId(user.getId())
                .stream().map(mapper::toModel)
                .toList();
    }

    @Transactional
    public ChatModel create(CreateChatPayload payload, UserModel user) {
        final var now = Date.from(Instant.now());
        final var model = new ChatModel(payload.name(), payload.description(), List.of(user));
        model.setId(null);
        model.setCreatedAt(now);
        model.setUpdatedAt(now);
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Transactional
    public void deleteById(long id) {
        getById(id).orElseThrow(ChatNotFoundException::new);
        repository.deleteById(id);
    }
}

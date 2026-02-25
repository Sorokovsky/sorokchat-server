package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.NewGroupPayload;
import com.sorokovsky.sorokchat.contract.UpdateGroupPayload;
import com.sorokovsky.sorokchat.exception.group.CanNotAddSelfException;
import com.sorokovsky.sorokchat.exception.group.GroupAlreadyExistsException;
import com.sorokovsky.sorokchat.exception.group.GroupConflictException;
import com.sorokovsky.sorokchat.exception.group.GroupNotFoundException;
import com.sorokovsky.sorokchat.mapper.GroupMapper;
import com.sorokovsky.sorokchat.model.GroupModel;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.repository.GroupsRepository;
import com.sorokovsky.sorokchat.utils.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupsService {
    private final GroupsRepository repository;
    private final UsersService usersService;
    private final GroupMapper mapper;

    @Transactional(readOnly = true)
    public List<GroupModel> getMyGroups(Long userId) {
        return repository.findAllByUser(userId).stream().map(mapper::toModel).toList();
    }

    @Transactional
    public GroupModel createGroup(UserModel me, NewGroupPayload payload) {
        final var users = new java.util.ArrayList<>(payload
                .addedMembers()
                .stream()
                .distinct()
                .map(usersService::getByIdRequired)
                .toList());
        if (users.stream().anyMatch(user -> me.getId().equals(user.getId()))) {
            throw new CanNotAddSelfException();
        }
        users.addLast(me);
        final var group = GroupModel
                .builder()
                .nickname(payload.nickname())
                .displayName(payload.displayName())
                .description(payload.description().orElse(null))
                .members(new HashSet<>(users))
                .build();
        try {
            return mapper.toModel(repository.save(mapper.toEntity(group)));
        } catch (DataIntegrityViolationException _) {
            throw new GroupAlreadyExistsException();
        }
    }

    @Transactional(readOnly = true)
    public List<GroupModel> getGroupsByTerm(String term) {
        if (Nickname.isNickname(term)) {
            return repository.findAllByNicknameLikeIgnoreCase("%" + Nickname.asTerm(term) + "%")
                    .stream()
                    .map(mapper::toModel).toList();
        } else {
            return repository.findAllByDisplayNameLikeIgnoreCase("%" + term + "%")
                    .stream()
                    .map(mapper::toModel)
                    .toList();
        }
    }

    @Transactional(readOnly = true)
    public Optional<GroupModel> getById(Long id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Transactional
    public void deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new GroupNotFoundException();
        }
    }

    @Transactional
    public GroupModel updateGroup(Long id, UpdateGroupPayload payload) {
        final var group = repository.findById(id).map(mapper::toModel).orElseThrow(GroupNotFoundException::new);
        if (payload.nickname().isPresent()) group.setNickname(payload.nickname().get());
        if (payload.displayName().isPresent()) group.setDisplayName(payload.displayName().get());
        if (payload.description().isPresent()) group.setDescription(payload.description().get());
        try {
            return mapper.toModel(repository.save(mapper.toEntity(group)));
        } catch (DataIntegrityViolationException _) {
            throw new GroupConflictException();
        }
    }
}

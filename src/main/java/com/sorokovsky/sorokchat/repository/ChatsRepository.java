package com.sorokovsky.sorokchat.repository;

import com.sorokovsky.sorokchat.entity.ChatEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatsRepository extends CrudRepository<ChatEntity, Long> {
    Optional<ChatEntity> findById(Long id);

    Optional<ChatEntity> findByName(String name);

    List<ChatEntity> findAllByMembersId(Long memberId);

    ChatEntity save(ChatEntity chatEntity);

    void deleteById(Long id);
}

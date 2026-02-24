package com.sorokovsky.sorokchat.repository;

import com.sorokovsky.sorokchat.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByNicknameLikeIgnoreCase(String nickname);

    Optional<UserEntity> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findById(Long id);

    List<UserEntity> findAllByDisplayNameLikeIgnoreCase(String displayName);

    List<UserEntity> findAllByDisplayName(String displayName);

    void deleteById(Long id);
}

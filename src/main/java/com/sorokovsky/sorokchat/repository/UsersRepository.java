package com.sorokovsky.sorokchat.repository;

import com.sorokovsky.sorokchat.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    void deleteById(Long id);

    UserEntity save(UserEntity userEntity);
}

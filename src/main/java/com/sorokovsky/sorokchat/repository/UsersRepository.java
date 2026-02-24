package com.sorokovsky.sorokchat.repository;

import com.sorokovsky.sorokchat.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    @Query("""
                    SELECT user FROM UserEntity user
                    WHERE LOWER(user.nickname) LIKE LOWER(CONCAT("%", :nickname, "%"))
            """)
    List<UserEntity> findByNicknameLikeIgnoreCase(@Param("nickname") String nickname);

    Optional<UserEntity> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserEntity> findById(Long id);

    @Query("""
                    SELECT user FROM UserEntity user
                    WHERE LOWER(user.displayName) LIKE LOWER(CONCAT("%", :displayName, "%"))
            """)
    List<UserEntity> findAllByDisplayNameLikeIgnoreCase(@Param("displayName") String displayName);

    List<UserEntity> findAllByDisplayName(String displayName);

    void deleteById(Long id);
}

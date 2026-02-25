package com.sorokovsky.sorokchat.repository;

import com.sorokovsky.sorokchat.entity.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupsRepository extends JpaRepository<GroupEntity, Long> {
    @Query("""
                SELECT g from GroupEntity g
                JOIN g.members member
                WHERE member.id = :userId
            """)
    List<GroupEntity> findAllByUser(@Param("userId") Long userId);

    Optional<GroupEntity> findById(Long id);

    boolean existsById(Long id);

    List<GroupEntity> findAllByNicknameLikeIgnoreCase(String nickname);

    List<GroupEntity> findAllByDisplayNameLikeIgnoreCase(String displayName);
}

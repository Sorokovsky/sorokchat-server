package com.sorokovsky.sorokchat.repository;

import com.sorokovsky.sorokchat.entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactsRepository extends JpaRepository<ContactEntity, Long> {
    @Query("""
                    SELECT contact FROM ContactEntity contact
                    WHERE contact.firstUser.id = :userId OR contact.secondUser.id = :userId
            """)
    List<ContactEntity> findContactsForUsers(@Param("userId") Long userId);

    @Query("""
                SELECT c FROM ContactEntity c
                WHERE (c.firstUser.id = :currentUserId AND (
                            LOWER(
                                CASE WHEN LOCATE('@', c.secondUser.nickname) = 1 
                                     THEN SUBSTRING(c.secondUser.nickname, 2) 
                                     ELSE c.secondUser.nickname 
                                END
                            ) LIKE LOWER(CONCAT('%', 
                                CASE WHEN LOCATE('@', :nickname) = 1 
                                     THEN SUBSTRING(:nickname, 2) 
                                     ELSE :nickname 
                                END, '%'))
                            OR LOWER(c.secondUser.displayName) LIKE LOWER(CONCAT('%', :nickname, '%'))
                       ))
                   OR (c.secondUser.id = :currentUserId AND (
                            LOWER(
                                CASE WHEN LOCATE('@', c.firstUser.nickname) = 1 
                                     THEN SUBSTRING(c.firstUser.nickname, 2) 
                                     ELSE c.firstUser.nickname 
                                END
                            ) LIKE LOWER(CONCAT('%', 
                                CASE WHEN LOCATE('@', :nickname) = 1 
                                     THEN SUBSTRING(:nickname, 2) 
                                     ELSE :nickname 
                                END, '%'))
                            OR LOWER(c.firstUser.displayName) LIKE LOWER(CONCAT('%', :nickname, '%'))
                       ))
            """)
    Optional<ContactEntity> findContact(@Param("currentUserId") Long currentUserId, @Param("nickname") String nickname);
}

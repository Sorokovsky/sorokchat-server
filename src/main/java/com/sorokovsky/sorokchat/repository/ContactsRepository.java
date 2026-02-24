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
                    SELECT contact FROM ContactEntity contact
                    WHERE (contact.firstUser.id = :currentUserId AND LOWER(contact.secondUser.nickname) LIKE LOWER(CONCAT("%", :nickname, "%")))
                    OR (contact.secondUser.id = :currentUserId AND LOWER(contact.firstUser.nickname) LIKE LOWER(CONCAT("%", :nickname, "%"))) 
            """)
    Optional<ContactEntity> findContact(@Param("currentUserId") Long currentUserId, @Param("nickname") String nickname);
}

package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.entity.ContactEntity;
import com.sorokovsky.sorokchat.exception.contact.ContactAlreadyExistsException;
import com.sorokovsky.sorokchat.mapper.ContactMapper;
import com.sorokovsky.sorokchat.mapper.UserMapper;
import com.sorokovsky.sorokchat.model.ContactModel;
import com.sorokovsky.sorokchat.model.UserModel;
import com.sorokovsky.sorokchat.repository.ContactsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactsRepository repository;
    private final ContactMapper mapper;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<ContactModel> getContactsByUser(Long userId) {
        return repository.findContactsForUsers(userId).stream().map(mapper::toModel).toList();
    }

    @Transactional(readOnly = true)
    public Optional<ContactModel> getContactByNickname(Long userId, String nickname) {
        return repository.findContact(userId, nickname).map(mapper::toModel);
    }

    @Transactional
    public ContactModel createContact(UserModel firstUser, UserModel secondUser) {
        try {
            return mapper.toModel(repository.save(ContactEntity.of(userMapper.toEntity(firstUser), userMapper.toEntity(secondUser))));
        } catch (DataIntegrityViolationException exception) {
            throw new ContactAlreadyExistsException();
        }
    }
}

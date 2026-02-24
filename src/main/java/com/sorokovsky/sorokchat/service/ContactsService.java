package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.mapper.ContactMapper;
import com.sorokovsky.sorokchat.repository.ContactsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactsService {
    private final ContactsRepository repository;
    private final ContactMapper mapper;
}

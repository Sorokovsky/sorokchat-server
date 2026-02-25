package com.sorokovsky.sorokchat.service;

import com.sorokovsky.sorokchat.contract.NewMessagePayload;
import com.sorokovsky.sorokchat.exception.base.InternalServerException;
import com.sorokovsky.sorokchat.exception.chat.YouAreNotInChatException;
import com.sorokovsky.sorokchat.exception.contact.ContactNotFoundException;
import com.sorokovsky.sorokchat.exception.group.GroupNotFoundException;
import com.sorokovsky.sorokchat.model.MessageModel;
import com.sorokovsky.sorokchat.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ChatsService {
    private static final String CONTACT_PREFIX = "contact-";
    private static final String GROUP_PREFIX = "group-";

    private final ContactsService contactsService;
    private final GroupsService groupsService;

    public MessageModel writeMessage(UserModel author, String chatId, NewMessagePayload payload) {
        final var id = extractId(chatId);
        final var now = Date.from(Instant.now());
        final var message = new MessageModel(now, now, payload.text(), payload.mac(), chatId, author);
        if (chatId.startsWith(CONTACT_PREFIX)) {
            final var contact = contactsService.getContactById(id).orElseThrow(ContactNotFoundException::new);
            if (!contact.hasUser(author)) {
                throw new YouAreNotInChatException();
            }
            return message;

        } else if (chatId.startsWith(GROUP_PREFIX)) {
            final var group = groupsService.getById(id).orElseThrow(GroupNotFoundException::new);
            if (!group.hasUser(author)) {
                throw new YouAreNotInChatException();
            }
            return message;
        } else {
            throw new YouAreNotInChatException();
        }
    }

    private Long extractId(String chatId) {
        if (chatId.startsWith(CONTACT_PREFIX)) {
            return parseId(chatId, CONTACT_PREFIX.length());
        }
        if (chatId.startsWith(GROUP_PREFIX)) {
            return parseId(chatId, GROUP_PREFIX.length());
        }
        throw new InternalServerException();
    }

    private Long parseId(String chatId, int prefixLength) {
        String idString = chatId.substring(prefixLength);
        try {
            return Long.parseLong(idString);
        } catch (NumberFormatException _) {
            throw new InternalServerException();
        }
    }
}

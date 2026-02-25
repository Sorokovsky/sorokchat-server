package com.sorokovsky.sorokchat.mapper;

import com.sorokovsky.sorokchat.contract.GetMessagePayload;
import com.sorokovsky.sorokchat.model.MessageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final UserMapper userMapper;

    public GetMessagePayload toGet(MessageModel model) {
        return new GetMessagePayload(
                model.createdAt(),
                model.updatedAt(),
                model.text(),
                model.mac(),
                userMapper.toGet(model.author()),
                model.chatId()
        );
    }
}

package com.springboot.springproject.kafka.service;

import com.springboot.springproject.kafka.dto.ChatMessageDto;
import com.springboot.springproject.kafka.model.ChatMessage;
import com.springboot.springproject.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;

    public void save(ChatMessageDto message) {
        ChatMessage entity = ChatMessage.builder()
                .clientId(message.getClientId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();


        repository.save(entity);
    }
}


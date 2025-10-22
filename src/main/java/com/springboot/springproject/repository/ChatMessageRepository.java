package com.springboot.springproject.repository;

import com.springboot.springproject.kafka.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}

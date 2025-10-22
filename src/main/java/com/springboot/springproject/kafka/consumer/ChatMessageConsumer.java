package com.springboot.springproject.kafka.consumer;

import com.springboot.springproject.kafka.dto.ChatMessageDto;
import com.springboot.springproject.kafka.service.ChatMessageService;
import com.springboot.springproject.kafka.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageConsumer {

    private final ChatMessageService chatMessageService;
    private final SseEmitterService sseEmitterService; // 추가

    @KafkaListener(
            topics = "chat-messages",
            groupId = "chat-group",
            containerFactory = "chatListenerFactory"
    )
    public void consume(@Payload ChatMessageDto message) throws InterruptedException {
        // Kafka 수신 로그
        log.info("Received message: {}", message);
        sseEmitterService.sendLog("[Kafka-CONSUME] " + message.toString());

        // DB 저장
        chatMessageService.save(message);

        // 저장 완료 로그
        sseEmitterService.sendLog("[Kafka-CONSUME] " + message.toString() + ": DB 저장 완료");
    }
}

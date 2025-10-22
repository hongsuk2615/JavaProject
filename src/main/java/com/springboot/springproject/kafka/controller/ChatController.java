package com.springboot.springproject.kafka.controller;

import com.springboot.springproject.kafka.dto.ChatMessageDto;
import com.springboot.springproject.kafka.service.SseEmitterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class ChatController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final SseEmitterService sseEmitterService;

    public ChatController(KafkaTemplate<String, Object> kafkaTemplate,
                          SseEmitterService sseEmitterService) {
        this.kafkaTemplate = kafkaTemplate;
        this.sseEmitterService = sseEmitterService;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public ChatMessageDto send(ChatMessageDto message) {
        System.out.println(message);
        String randomKey = UUID.randomUUID().toString();
        kafkaTemplate.send("chat-messages", randomKey, message);
        sseEmitterService.sendLog("[Kafka-PROD] " + message.toString());
        return message;
    }

    @GetMapping("/api/client-info")
    @ResponseBody
    public Map<String, String> getClientInfo(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }

        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }

        Map<String, String> map = new HashMap<>();
        map.put("ip", ip);
        return map;
    }


}

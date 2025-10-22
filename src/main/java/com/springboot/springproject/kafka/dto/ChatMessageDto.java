package com.springboot.springproject.kafka.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ChatMessageDto {
    public String clientId;
    public String clientIp;
    public String content;
    public LocalDateTime timestamp;

    public ChatMessageDto() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessageDto(String clientId, String clientIp, String content) {
        this.clientId = clientId;
        this.clientIp = clientIp;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}

package com.springboot.springproject.kafka.controller;

import com.springboot.springproject.kafka.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class LogStreamController {

    private final SseEmitterService sseEmitterService;

    @GetMapping("/logs/stream")
    public SseEmitter stream() {
        return sseEmitterService.createEmitter();
    }
}

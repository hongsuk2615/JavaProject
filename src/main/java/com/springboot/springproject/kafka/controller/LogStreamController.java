package com.springboot.springproject.kafka.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class LogStreamController {

    @GetMapping("/logs/stream")
    public SseEmitter stream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        new Thread(() -> {
            try {
                while (true) {
                    emitter.send("log message");
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                emitter.complete();
            }
        }).start();

        return emitter;
    }
}

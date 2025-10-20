package com.springboot.springproject.kafka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KafkaController {
    @GetMapping("/kafka")
    public String index(){
        return "kafka/kafka";
    }
}

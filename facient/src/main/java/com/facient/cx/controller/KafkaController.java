package com.facient.cx.controller;


import com.facient.cx.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/kafka")
@RestController
public class KafkaController {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaController.class);
    private final KafkaProducer producer;

    public KafkaController(KafkaProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/send")
    public String send(@RequestParam("message") String message) {
        LOGGER.info("Controller: {}",message);
        producer.sendMessage(message);
        return "Message sent: " + message;
    }
}

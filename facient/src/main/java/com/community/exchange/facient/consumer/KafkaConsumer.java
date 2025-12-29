package com.community.exchange.facient.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "test-topic", groupId = "my-group")
    public void listen(String message) {
        String ans = "I am fine.";
        LOGGER.info("Message Received: Answer: {}{}", message,ans);
    }
}


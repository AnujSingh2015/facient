package com.facient.cx.producer;

import com.facient.cx.entity.dto.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "payment-request";

    public void sendPaymentRequest(PaymentRequest request) {
        kafkaTemplate.send(TOPIC, request);
        System.out.println("Payment request sent for order: " + request.getOrderId());
    }
}

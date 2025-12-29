package com.facient.cx.producer;

import com.facient.cx.entity.dto.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentStatusProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "payment-status";

    public void sendPaymentStatus(PaymentStatus status) {
        kafkaTemplate.send(TOPIC, status);
        System.out.println("Payment status sent: " + status.getOrderId());
    }
}

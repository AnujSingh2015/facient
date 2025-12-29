package com.facient.cx.consumer;

import com.facient.cx.entity.dto.PaymentRequest;
import com.facient.cx.entity.dto.PaymentStatus;
import com.facient.cx.producer.PaymentStatusProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentConsumer {

    @Autowired
    private PaymentStatusProducerService statusProducer;

    @KafkaListener(topics = "payment-request", groupId = "payment-group")
    public void processPayment(PaymentRequest request) {
        System.out.println("Processing payment for order: " + request.getOrderId());

        // Simulate payment logic
        PaymentStatus status = new PaymentStatus();
        status.setOrderId(request.getOrderId());
        status.setTransactionId(UUID.randomUUID().toString());
        status.setStatus("SUCCESS");

        // Send status update
        statusProducer.sendPaymentStatus(status);
    }
}


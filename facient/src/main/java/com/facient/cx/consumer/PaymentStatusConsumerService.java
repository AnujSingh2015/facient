package com.facient.cx.consumer;

import com.facient.cx.entity.dto.PaymentStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentStatusConsumerService {

    @KafkaListener(topics = "payment-status", groupId = "order-group")
    public void updateOrder(PaymentStatus status) {
        System.out.println("Payment Completed for order " + status.getOrderId() +
                " - Status: " + status.getStatus());
    }
}

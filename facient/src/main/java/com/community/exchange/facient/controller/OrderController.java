package com.community.exchange.facient.controller;

import com.community.exchange.facient.entity.dto.PaymentRequest;
import com.community.exchange.facient.producer.PaymentProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private PaymentProducer producer;

    @PostMapping("/pay")
    public String pay(@RequestBody PaymentRequest request) {
        producer.sendPaymentRequest(request);
        return "Payment requested for Order: " + request.getOrderId();
    }
}

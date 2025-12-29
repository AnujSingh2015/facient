package com.facient.cx.entity.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderId;
    private double amount;
    private String paymentMethod;
}

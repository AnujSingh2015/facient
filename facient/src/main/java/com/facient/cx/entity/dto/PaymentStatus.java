package com.facient.cx.entity.dto;

import lombok.Data;

@Data
public class PaymentStatus {
    private String orderId;
    private String status;   // SUCCESS / FAILED
    private String transactionId;
}

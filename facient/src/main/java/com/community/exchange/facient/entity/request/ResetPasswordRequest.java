package com.community.exchange.facient.entity.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String username;
    private String newPassword;

}

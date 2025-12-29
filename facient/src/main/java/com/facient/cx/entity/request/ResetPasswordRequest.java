package com.facient.cx.entity.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String username;
    private String newPassword;

}

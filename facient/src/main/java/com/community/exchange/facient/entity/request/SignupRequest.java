package com.community.exchange.facient.entity.request;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String role;
    private String email;
    private String securityQuestion;
    private String securityAnswer;
}

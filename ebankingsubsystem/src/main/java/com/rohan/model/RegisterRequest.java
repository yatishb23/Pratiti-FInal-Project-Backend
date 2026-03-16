package com.rohan.model;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String loginPassword;
    private String transactionPassword;
    private String mobile;
    private String email;
    private String role;
}
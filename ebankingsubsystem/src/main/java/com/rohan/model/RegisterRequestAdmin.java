package com.rohan.model;

import lombok.Data;

@Data
public class RegisterRequestAdmin {

    private String username;
    private String mobile;
    private String email;
    private String role;
}
package com.rohan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private Long userId;

    private String username;

    private String loginPassword;

    private String transactionPassword;

    private String telephoneNumber;

    private String mobile;

    private String email;

    private String role;
    
    private boolean isActive;
    
    private Integer loginAttempt;
    
    private boolean isCreatedByAdmin;
}

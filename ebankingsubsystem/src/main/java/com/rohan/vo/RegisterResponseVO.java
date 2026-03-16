package com.rohan.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseVO {

    private String username;

    private String mobile;

    private String email;

    private String role; 

    private boolean isActive=false;
    
    private Integer loginAttempt=0;
    
    private boolean isCreatedByAdmin;
    
    private String loginPassword;
    
    private String transactionPassword;
}

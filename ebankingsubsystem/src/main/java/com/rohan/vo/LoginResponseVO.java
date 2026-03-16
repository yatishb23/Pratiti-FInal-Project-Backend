package com.rohan.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseVO {
	
	private Long userId;

    private String username;

    private String telephoneNumber;

    private String mobile;

    private String email;

    private String role; 

    private boolean isActive;
    
    private Integer loginAttempt;
    
    private boolean isValid=false;
    
    private String message;
    
    private boolean isPresent=true;
    
    private boolean isCreatedByAdmin;
    
    public LoginResponseVO(boolean pres){
    	this.isPresent=pres;
    }
}

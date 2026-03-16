package com.sne.dto;

import lombok.Data;

import lombok.*;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryDto {
     
	 @NotBlank(message = "Account number is required")
	    @Size(min = 11, max = 11, message = "Account number must be exactly 11 digits")
	    @Pattern(regexp = "\\d{11}", message = "Account number must contain only digits")

    private String beneficiaryAccountNo; 
    private String accountType;
  
    private String ifscCode;
    private String beneficiaryName;
    private String emailId;
    private String status;
    private String branchName;
    private LocalDateTime addedTimestamp;
    private String nickName;
    private Long userId;          
}
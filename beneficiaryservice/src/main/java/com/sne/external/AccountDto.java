package com.sne.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String accountNumber; // The 11-digit string (e.g., ACC12345678)
    private String accountType; 
    // This will be "BENEFICIARY"
    private String accountStatus; 
    private Long userId;          // To link who added this beneficiary
    private String branchName;
    private BigDecimal availableBalance; // Usually set to 0.00 for external beneficiaries
    
    // Optional: Add IFSC if you're dealing with other banks
//    private String ifscCode; 
}


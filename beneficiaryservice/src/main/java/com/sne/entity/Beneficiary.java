package com.sne.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary {
	@Id
    @Column(unique = true, nullable = false)
    @Size(min = 11, max = 11, message = "Account number must be exactly 11 digits")
    @Pattern(regexp = "^\\d{11}$", message = "Account number must be 11 numeric digits")
    private String beneficiaryAccountNo;

    private String accountType;
    private String ifscCode;
    private String beneficiaryName;
    private String branchName;
    private String emailId;
    private String status;
    private LocalDateTime addedTimestamp;
    @Column(name = "nick_name") 
    private String nickName;
//    private String nickName;

   
    @Column(nullable = false)
    private Long userId;
}
package com.rohan.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String loginPassword;

    @Column(nullable = false)
    private String transactionPassword;

    private String telephoneNumber;

    @Column(nullable = false)
    private String mobile;

    @Column(unique=true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String role; 

    private boolean isActive=false;
    
    private Integer loginAttempt=3;
    
    private boolean isCreatedByAdmin=false;
}
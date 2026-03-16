

package com.sne.service;

import com.sne.exception.*;
import com.sne.model.AccountStatus;
import com.sne.model.AccountType;
import com.sne.model.dto.AccountDto;
import com.sne.model.dto.AccountStatusUpdate;
import com.sne.model.dto.external.TransactionResponse;
import com.sne.model.dto.response.Response;
import com.sne.model.entity.Account;
import com.sne.model.mapper.AccountMapper;
import com.sne.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService  {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper = new AccountMapper();

    @Value("${spring.application.ok:200}")
    private String success;


    private static final Map<String, String> BRANCH_IFSC_MAP = Map.of(
        "AUNDH", "BANK0001234",
        "HINJEWADI", "BANK0005678",
        "SHIVAJINAGAR", "BANK0009012",
        "BANER", "BANK0005682"
    );

    public Response createAccount(AccountDto accountDto) {
        // 1. Validate Branch and Get IFSC
        String branch = accountDto.getBranchName() != null ? accountDto.getBranchName().toUpperCase() : "";
        String ifsc = BRANCH_IFSC_MAP.get(branch);

        if (ifsc == null) {
            return Response.builder()
                    .responseCode("400")
                    .message("Invalid Branch. Allowed: " + BRANCH_IFSC_MAP.keySet())
                    .build();
        }

        Account account = new Account();
        account.setBranchName(branch);
        account.setIfscCode(ifsc);

        // 2. Handle Account Number & Type Logic
        if (accountDto.getAccountNumber() == null) {
            // SELF Account Logic
            String timeStr = String.valueOf(System.currentTimeMillis()); 
            account.setAccountNumber(timeStr.substring(timeStr.length() - 11));
            account.setAccountStatus(AccountStatus.INACTIVE);
            account.setAccountType(AccountType.SELF);
            account.setAvailableBalance(BigDecimal.ZERO); // Per your request: SELF = 0
            
           
            account.setCardNumber(null); 
        } else {
            // BENEFICIARY Account Logic
            if (accountRepository.existsByAccountNumber(accountDto.getAccountNumber())) {
                return Response.builder().responseCode("409").message("Account already exists").build();
            }
            account.setAccountNumber(accountDto.getAccountNumber());
            account.setAccountType(AccountType.BENEFICIARY);
            account.setAccountStatus(AccountStatus.ACTIVE);
            account.setAvailableBalance(accountDto.getAvailableBalance());
            // Beneficiaries usually don't get a card issued by OUR system
            account.setCardNumber(null); 
        }

        account.setUserId(accountDto.getUserId());
        accountRepository.save(account);

        return Response.builder()
                .responseCode("201")
                .message(account.getAccountType() + " account created with Card: " + account.getCardNumber())
                .build();
    }
    
    
    
    public Response applyForCard(String accountNumber) {
        // 1. Fetch the account
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElse(null);

        if (account == null) {
            return Response.builder().responseCode("404").message("Account not found").build();
        }

        // 2. Check if account is ACTIVE
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            return Response.builder()
                    .responseCode("403")
                    .message("Cannot apply for card. Account status is: " + account.getAccountStatus())
                    .build();
        }

        // 3. Check if card already exists
        if (account.getCardNumber() != null) {
            return Response.builder()
                    .responseCode("409")
                    .message("Card already issued for this account")
                    .build();
        }

        // 4. Generate and save
        account.setCardNumber(generateCardNumber());
        accountRepository.save(account);

        return Response.builder()
                .responseCode("200")
                .message("Card generated successfully: " + account.getCardNumber())
                .build();
    }

   
    private String generateCardNumber() {
        long first15 = (long) (Math.random() * 100000000000000L) + 500000000000000L; // Start with 5 for Mastercard style
       
        return String.valueOf(first15) + (int)(Math.random() * 10);
    }


    @Override
    public Response updateStatus(String accountNumber, AccountStatusUpdate accountUpdate) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
//                    if (account.getAccountStatus().equals(AccountStatus.CLOSED)) {
//                        throw new AccountStatusException("Account is already closed");
//                    }
                    // Specific logic for activation: check minimum balance
//                    if (accountUpdate.getAccountStatus().equals(AccountStatus.ACTIVE) && 
//                        account.getAvailableBalance().compareTo(BigDecimal.valueOf(1000)) < 0) {
//                        throw new RuntimeException("Minimum balance of Rs.1000 is required to activate");
//                    }
                    
                    account.setAccountStatus(accountUpdate.getAccountStatus());
                    accountRepository.save(account);
                    return Response.builder()
                            .responseCode(success)
                            .message("Account status updated to " + accountUpdate.getAccountStatus())
                            .build();
                }).orElseThrow(() -> new ResourceNotFound("Account not found"));
    }

    @Override
    public AccountDto readAccountByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    AccountDto dto = accountMapper.convertToDto(account);
                    dto.setAccountType(account.getAccountType().toString());
                    dto.setAccountStatus(account.getAccountStatus().toString());
                    return dto;
                }).orElseThrow(() -> new ResourceNotFound("Account not found: " + accountNumber));
    }

    @Override
    public Response updateAccount(String accountNumber, AccountDto accountDto) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    // Update only specific fields, don't allow changing AccountNumber via update
                    account.setBranchName(accountDto.getBranchName());
                    accountRepository.save(account);
                    return Response.builder()
                            .responseCode(success)
                            .message("Account updated successfully")
                            .build();
                }).orElseThrow(() -> new ResourceNotFound("Account not found"));
    }

    @Override
    public String getBalance(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> account.getAvailableBalance().toString())
                .orElseThrow(() -> new ResourceNotFound("Account not found"));
    }

    @Override
    public Response closeAccount(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    if (account.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0) {
                        throw new AccountClosingException("Balance should be zero before closing account. Current: " + account.getAvailableBalance());
                    }
                    account.setAccountStatus(AccountStatus.INACTIVE);
                    accountRepository.save(account);
                    return Response.builder()
                            .responseCode(success)
                            .message("Account closed successfully")
                            .build();
                }).orElseThrow(() -> new ResourceNotFound("Account not found"));
    }

//    @Override
//    public AccountDto readAccountByUserId(Long userId) {
//        return accountRepository.findAccountByUserId(userId)
//                .map(account -> {
//                    AccountDto dto = accountMapper.convertToDto(account);
//                    dto.setAccountStatus(account.getAccountStatus().toString());
//                    dto.setAccountType(account.getAccountType().toString());
//                    return dto;
//                }).orElseThrow(() -> new ResourceNotFound("No account found for Customer ID: " + userId));
//    }
    
//    @Override
//    public List<AccountDto> readAccountsByUserId(Long userId) {
//        // We filter by userId AND the 'SELF' account type
//        Optional<List<Account>> all = accountRepository.findByUserIdAndAccountType(userId, AccountType.SELF); 
////               return all .stream()
////                .map(account -> {
////                    AccountDto dto = accountMapper.convertToDto(account);
////                    dto.setAccountStatus(account.getAccountStatus().toString());
////                    dto.setAccountType(account.getAccountType().toString());
////                    return dto;
////                })
////                .collect(Collectors.toList());
//        return all.stream()
//                .filter(account -> account.getAccountType() == AccountType.SELF) // Filtering logic here
//                .map(account -> {
//                    AccountDto dto = accountMapper.convertToDto(account);
//                    dto.setAccountStatus(account.getAccountStatus().toString());
//                    dto.setAccountType(account.getAccountType().toString());
//                    return dto;
//                })
//                .collect(Collectors.toList());
//    }
    
    @Override
    public List<AccountDto> readAccountsByUserId(Long userId) {
        return accountRepository.findByUserIdAndAccountType(userId, AccountType.SELF)
                .stream() // This creates a Stream<List<Account>>
                .flatMap(List::stream) // This turns it into a Stream<Account>
                .map(account -> {
                    AccountDto dto = accountMapper.convertToDto(account);
                    dto.setAccountStatus(account.getAccountStatus().toString());
                    dto.setAccountType(account.getAccountType().toString());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // INTERNAL METHOD FOR BENEFICIARY SERVICE
    @Override
	public boolean accountExists(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber).isPresent();
    }

    
}


package com.sne.controller;

import com.sne.model.dto.AccountDto;
import com.sne.model.dto.AccountStatusUpdate;
import com.sne.model.dto.external.TransactionResponse;
import com.sne.model.dto.response.Response;
import com.sne.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/add")
    public ResponseEntity<Response> createAccount(@RequestBody AccountDto accountDto) {
        log.info("REST request to create Account");
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }
    
    
    @PatchMapping("/applycard/{accountNumber}")
    public ResponseEntity<Response> applyForCard(@PathVariable String accountNumber) {
        log.info("Received request to apply for card for account: {}", accountNumber);
        
        Response response = accountService.applyForCard(accountNumber);
        
        // Return 200 OK for success, or the specific error code returned by service
        if ("200".equals(response.getResponseCode())) {
            return ResponseEntity.ok(response);
        } else if ("404".equals(response.getResponseCode())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if ("403".equals(response.getResponseCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDto> readByAccountNumber(@PathVariable String accountNumber) {
        log.info("REST request to get Account : {}", accountNumber);
        return ResponseEntity.ok(accountService.readAccountByAccountNumber(accountNumber));
    }

    @PatchMapping("/status/{accountNumber}")
    public ResponseEntity<Response> updateAccountStatus(
            @PathVariable String accountNumber,
            @RequestBody AccountStatusUpdate accountStatusUpdate) {
        log.info("REST request to update status for Account : {}", accountNumber);
        return ResponseEntity.ok(accountService.updateStatus(accountNumber, accountStatusUpdate));
    }

    /**
     * Update account details.
     */
//    @PutMapping("/{accountNumber}")
//    public ResponseEntity<Response> updateAccount(
//            @PathVariable String accountNumber, 
//            @RequestBody AccountDto accountDto) {
//        log.info("REST request to update Account : {}", accountNumber);
//        return ResponseEntity.ok(accountService.updateAccount(accountNumber, accountDto));
//    }

    /**
     * Get available balance for a specific account.
     */
    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<String> getAccountBalance(@PathVariable String accountNumber) {
        log.info("REST request to get balance for Account : {}", accountNumber);
        return ResponseEntity.ok(accountService.getBalance(accountNumber));
    }

    

    /**
     * Soft close an account.
     */
//    @PutMapping("/{accountNumber}/close")
//    public ResponseEntity<Response> closeAccount(@PathVariable String accountNumber) {
//        log.info("REST request to close Account : {}", accountNumber);
//        return ResponseEntity.ok(accountService.closeAccount(accountNumber));
//    }

    /**
     * Get account details linked to a Customer ID.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDto>> readAccountByUserId(@PathVariable Long userId){
        log.info("REST request to get Account for Customer ID: {}", userId);
        return ResponseEntity.ok(accountService.readAccountsByUserId(userId));
    }
}
package com.sne.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sne.dto.response.Response;
import com.sne.external.AccountDto;

@FeignClient(name = "ACCOUNTSERVICE") 
public interface AccountClient {
    
   
    @PostMapping("/api/accounts/add")
    Response createAccount(@RequestBody AccountDto accountDto);

    @GetMapping("/api/accounts/{accountNumber}")
    Response getAccountByNumber(@PathVariable String accountNumber);
}
package com.sne.service;

import java.util.List;

import com.sne.model.dto.AccountDto;
import com.sne.model.dto.AccountStatusUpdate;
import com.sne.model.dto.external.TransactionResponse;
import com.sne.model.dto.response.Response;

public interface AccountService {

	Response createAccount(AccountDto accountDto);

	Response updateStatus(String accountNumber, AccountStatusUpdate accountUpdate);

	AccountDto readAccountByAccountNumber(String accountNumber);

	Response updateAccount(String accountNumber, AccountDto accountDto);

	String getBalance(String accountNumber);

	Response closeAccount(String accountNumber);
	
	boolean accountExists(String accountNumber);

	List<AccountDto> readAccountsByUserId(Long userId);

	Response applyForCard(String accountNumber);

}
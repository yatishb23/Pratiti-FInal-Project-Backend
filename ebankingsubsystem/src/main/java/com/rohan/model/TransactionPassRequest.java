package com.rohan.model;

import lombok.Data;

@Data
public class TransactionPassRequest {
	private Long userId;
	private String transactionPassword;
}

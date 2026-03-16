package com.rohan.model;

import lombok.Data;

@Data
public class ResetPasswordRequest {

	private String email;
	private String password;
}

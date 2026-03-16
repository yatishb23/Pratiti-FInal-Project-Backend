package com.rohan.model;

import lombok.Data;

@Data
public class UpdateUserRequest {
		private String username;
		private String mobile;
	    private String email;
	    private String telephoneNumber;
	}

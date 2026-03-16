package com.rohan.service;

import com.rohan.model.ChangePasswordRequest;
import com.rohan.model.TransactionPassRequest;
import com.rohan.model.UpdateUserRequest;

public interface UserService {

	boolean updateUserProfile(UpdateUserRequest userProfile);
	boolean updateLoginPassword(ChangePasswordRequest userDetails);
	boolean updateTransactionPassword(ChangePasswordRequest userDetails);
	boolean validateTransactionPassword(TransactionPassRequest tranDetails);
	boolean resetTransactionPassword(String email, String newPassword);
	boolean resetLoginPassword(String email, String newPassword);
}
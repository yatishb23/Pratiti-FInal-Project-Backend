package com.rohan.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.model.ChangePasswordRequest;
import com.rohan.model.ResetPasswordRequest;
import com.rohan.model.TransactionPassRequest;
import com.rohan.model.UpdateUserRequest;
import com.rohan.model.ValidateTranResponse;
import com.rohan.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService service;

//	@GetMapping("/check")
//	public ResponseEntity<String> check(){
//		return ResponseEntity.ok().body("success");
//	}
	@PutMapping("/updateprofile")
	public ResponseEntity<Map<String, String>> updateProfile(@RequestBody UpdateUserRequest userProfile) {

		log.info("UserController: Inside updateProfile");

		boolean check = service.updateUserProfile(userProfile);

		Map<String, String> response = new HashMap<>();

		if (check) {
			response.put("message", "Profile Updated Successfully..");
			return ResponseEntity.ok(response);
		}

		response.put("message", "Unable to update profile");
		return ResponseEntity.ok(response);
	}

	@PutMapping("/updateloginpassword")
	public ResponseEntity<Map<String, String>> updateLoginPassword(@RequestBody ChangePasswordRequest userDetails) {
		log.info("UserController: Inside updateLoginPassword");
		boolean check = service.updateLoginPassword(userDetails);
		Map<String, String> response = new HashMap<>();
		if (check) {
			response.put("message", "Login Password Change Successfully..");
			return ResponseEntity.ok(response);
		}
		response.put("message", "Unable to change Login Password..");
		return ResponseEntity.ok(response);
	}

	@PutMapping("/updatetransactionpassword")
	public ResponseEntity<Map<String, String>> updateTransactionPassword(
			@RequestBody ChangePasswordRequest userDetails) {
		log.info("UserController: Inside updateTransactionPassword");
		boolean check = service.updateTransactionPassword(userDetails);
		Map<String, String> response = new HashMap<>();
		if (check) {
			response.put("message", "Transaction Password Change Successfully..");
			return ResponseEntity.ok(response);
		}
		response.put("message", "Unable to change Transaction Password..");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/validatetransactionpassword")
	public boolean validateTransactionPassword(@RequestBody TransactionPassRequest tranDetails) {
		log.info("UserController: Inside validateTransactionPassword");
		return service.validateTransactionPassword(tranDetails);
	}

	@PostMapping("/validatetranpassword")
	public ResponseEntity<ValidateTranResponse> validateTranPassword(@RequestBody TransactionPassRequest tranDetails) {

		log.info("UserController: Inside validateTransactionPassword");

		boolean check = service.validateTransactionPassword(tranDetails);
		log.info("UserController: Inside validateTransactionPassword check:" + check);
		if (check) {
			ValidateTranResponse response = new ValidateTranResponse("Correct Password", true);
			return ResponseEntity.ok(response);
		} else {
			ValidateTranResponse response = new ValidateTranResponse("Incorrect Password", false);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PutMapping("/resetloginpassword")
	public ResponseEntity<Map<String, String>> resetLoginPassword(@RequestBody ResetPasswordRequest request) {

		log.info("UserController: Inside resetLoginPassword");

		boolean check = service.resetLoginPassword(request.getEmail(), request.getPassword());

		Map<String, String> response = new HashMap<>();

		if (check) {
			response.put("message", "Login Password Reset Successfully..");
			return ResponseEntity.ok(response);
		}

		response.put("message", "Unable to reset Login Password");
		return ResponseEntity.badRequest().body(response);
	}

	@PutMapping("/resettransactionpassword")
	public ResponseEntity<Map<String, String>> resetTransactionPassword(@RequestBody ResetPasswordRequest request) {

		log.info("UserController: Inside resetTransactionPassword");

		boolean check = service.resetTransactionPassword(request.getEmail(), request.getPassword());

		Map<String, String> response = new HashMap<>();

		if (check) {
			response.put("message", "Transaction Password Reset Successfully..");
			return ResponseEntity.ok(response);
		}

		response.put("message", "Unable to reset Transaction Password");
		return ResponseEntity.badRequest().body(response);
	}

}

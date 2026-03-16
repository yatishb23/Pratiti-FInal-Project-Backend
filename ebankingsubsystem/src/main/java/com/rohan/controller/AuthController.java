package com.rohan.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rohan.model.LoginRequest;
import com.rohan.model.RegisterRequest;
import com.rohan.service.AuthService;
import com.rohan.vo.LoginResponseVO;
import com.rohan.vo.RegisterResponseVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponseVO> registerRequest(@RequestBody RegisterRequest request) {
		log.info("AuthController: Inside registerRequest method");
		Optional<RegisterResponseVO>optional=authService.registerNewUser(request);
		if(optional.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok().body(optional.get());
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseVO> loginRequest(@RequestBody LoginRequest login) {
		log.info("AuthController: Inside loginRequest method");
		Optional<LoginResponseVO>optional=authService.validateLogin(login);
		if(!optional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(optional.get());
	}
}

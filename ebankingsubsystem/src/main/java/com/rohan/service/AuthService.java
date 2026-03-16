package com.rohan.service;

import java.util.Optional;


import com.rohan.model.LoginRequest;
import com.rohan.model.RegisterRequest;
import com.rohan.vo.LoginResponseVO;
import com.rohan.vo.RegisterResponseVO;
//import com.rohan.vo.LoginResponseVO;

public interface AuthService {
	public Optional<LoginResponseVO> validateLogin(LoginRequest loginDetails);
	public Optional<RegisterResponseVO> registerNewUser(RegisterRequest registerDetails);
}

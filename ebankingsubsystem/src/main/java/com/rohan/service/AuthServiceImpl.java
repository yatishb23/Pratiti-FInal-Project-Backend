package com.rohan.service;


import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rohan.model.UserModel;
import com.rohan.dao.AuthDao;
import com.rohan.entity.UserEntity;
import com.rohan.exception.AccountNotActiveException;
import com.rohan.exception.UsernameNotAvailableException;
import com.rohan.model.LoginRequest;
import com.rohan.model.RegisterRequest;
import com.rohan.vo.LoginResponseVO;
import com.rohan.vo.RegisterResponseVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
	private final PasswordEncoder encoder;
	private final ModelMapper mapper;
	private final AuthDao authDao;
	@Override
	public Optional<LoginResponseVO> validateLogin(LoginRequest loginDetails) {
		log.info("AuthService: Inside ValidateLogin");
		Optional<UserEntity>optional=authDao.findByUsername(loginDetails.getUsername());
		if(!optional.isPresent()) {
			LoginResponseVO output=new LoginResponseVO(false);
			return Optional.of(output);
		}
		UserModel user=mapper.map(optional.get(),UserModel.class);
		if(user.isActive() && user.getLoginAttempt()>0) {
		boolean isTrue=encoder.matches(loginDetails.getPassword(),user.getLoginPassword());
		if(!isTrue) {
			optional.get().setLoginAttempt(user.getLoginAttempt()-1);
			authDao.save(optional.get());
			LoginResponseVO output=mapper.map(user, LoginResponseVO.class);
			output.setMessage("Incorrect Password!!");
			output.setLoginAttempt(user.getLoginAttempt()-1);
			return Optional.of(output);
		}
		LoginResponseVO output=mapper.map(user, LoginResponseVO.class);
		optional.get().setLoginAttempt(3);
		authDao.save(optional.get());
		output.setValid(true);
		output.setLoginAttempt(3);
		output.setMessage("Login Successful..");
		return Optional.of(output);
		}else {
			throw new AccountNotActiveException("Your Account has been Blocked!!! Please contact Admin..");
		}
	}
	@Override
	public Optional<RegisterResponseVO> registerNewUser(RegisterRequest registerDetails) {
		log.info("AuthService: Inside RegisterNewUser");
		Optional<UserEntity>optional=authDao.findByUsername(registerDetails.getUsername());
		if(optional.isPresent()) {
			throw new UsernameNotAvailableException("Username Not Available!!");
		}
		Optional<UserEntity>optional1=authDao.findByEmail(registerDetails.getEmail());
		if(optional1.isPresent()) {
			throw new UsernameNotAvailableException("Email Not Available!!");
		}
		UserEntity newUser=mapper.map(registerDetails, UserEntity.class);
		newUser.setUsername(registerDetails.getUsername().toLowerCase());
		newUser.setLoginPassword(encoder.encode(newUser.getLoginPassword()));
		newUser.setTransactionPassword(encoder.encode(newUser.getTransactionPassword()));
		UserEntity addedUser=authDao.save(newUser);
		RegisterResponseVO validated=mapper.map(addedUser, RegisterResponseVO.class);
		log.info(addedUser.toString());
		return Optional.of(validated);
	}
	
}

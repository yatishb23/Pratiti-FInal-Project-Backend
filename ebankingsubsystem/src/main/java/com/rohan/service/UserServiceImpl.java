package com.rohan.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rohan.dao.UserDao;
import com.rohan.entity.UserEntity;
import com.rohan.exception.UnableToUpdateProfileException;
import com.rohan.exception.UsernameNotFoundException;
import com.rohan.model.ChangePasswordRequest;
import com.rohan.model.TransactionPassRequest;
import com.rohan.model.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	

	private final PasswordEncoder encoder;
	private final UserDao userDao;
	@Override
	public boolean updateUserProfile(UpdateUserRequest userProfile) {
		Optional<UserEntity>optional=userDao.findByUsername(userProfile.getUsername());
		if(!optional.isPresent()) {
			throw new UnableToUpdateProfileException("Unable to update user profile");
		}
		UserEntity user=optional.get();
		user.setEmail(userProfile.getEmail());
		user.setTelephoneNumber(userProfile.getTelephoneNumber());
		user.setMobile(userProfile.getMobile());
		UserEntity check=userDao.save(user);
		userDao.save(check);
		return true;
	}
	@Override
	public boolean updateLoginPassword(ChangePasswordRequest userDetails) {
		
		log.info("AuthService: Inside updateLoginPassword");
	    Optional<UserEntity> optional = userDao.findByUsername(userDetails.getUsername());

	    if (!optional.isPresent()) {
	        throw new UsernameNotFoundException("Username Not Found!!");
	    }

	    UserEntity user = optional.get();
	    boolean isTrue = encoder.matches( userDetails.getOldPassword(),user.getLoginPassword());
	    
	    if (!isTrue) {
	        return false;
	    }
	    user.setLoginPassword(encoder.encode(userDetails.getNewPassword()));
	    userDao.save(user);
	    return true;
	}
	@Override
	public boolean updateTransactionPassword(ChangePasswordRequest userDetails) {
		log.info("AuthService: Inside updateTransactionPassword");
	    Optional<UserEntity> optional = userDao.findByUsername(userDetails.getUsername());

	    if (!optional.isPresent()) {
	        throw new UsernameNotFoundException("Username Not Found!!");
	    }

	    UserEntity user = optional.get();
	    boolean isTrue = encoder.matches( userDetails.getOldPassword(),user.getTransactionPassword());
	    
	    if (!isTrue) {
	        return false;
	    }
	    user.setTransactionPassword(encoder.encode(userDetails.getNewPassword()));
	    userDao.save(user);
	    return true;
	}
	@Override
	public boolean validateTransactionPassword(TransactionPassRequest tranDetails) {
		Optional<UserEntity> optional=userDao.findById(tranDetails.getUserId());
		UserEntity user = optional.get();
	    return encoder.matches( tranDetails.getTransactionPassword(),user.getTransactionPassword());
	}
	@Override
	public boolean resetLoginPassword(String email, String newPassword) {
		   Optional<UserEntity> userOpt = userDao.findByEmail(email);

		    if(userOpt.isPresent()){
		        UserEntity user = userOpt.get();
		        user.setLoginPassword(encoder.encode(newPassword));
		        userDao.save(user);
		        return true;
		    }

		    return false;
	}
	@Override
	public boolean resetTransactionPassword(String email, String newPassword) {
		   Optional<UserEntity> userOpt = userDao.findByEmail(email);

		    if(userOpt.isPresent()){
		        UserEntity user = userOpt.get();
		        user.setTransactionPassword(encoder.encode(newPassword));
		        userDao.save(user);
		        return true;
		    }

		    return false;
	}
}

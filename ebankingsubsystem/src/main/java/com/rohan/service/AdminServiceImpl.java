package com.rohan.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rohan.dao.AdminDao;
import com.rohan.entity.UserEntity;
import com.rohan.exception.UsernameNotAvailableException;
import com.rohan.model.RegisterRequestAdmin;
import com.rohan.vo.RegisterResponseVO;
import com.rohan.vo.UserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	

	private final ModelMapper mapper;
	private final AdminDao adminDao;
	private final PasswordEncoder encoder;
	
	@Override
	public Optional<List<UserVO>> getAllUsers(){
		log.info("AdminService: Inside getAllUSers");
		List<UserEntity> listEntity=adminDao.getAllUsers();
		List<UserVO> listVO=listEntity.stream()
				.map(entity->mapper.map(entity, UserVO.class)).toList();;
		return Optional.of(listVO);
	}

	@Override
	public boolean changeUserStatus(String username) {
		log.info("AdminService: Inside changeUserStatus");
		Optional<UserEntity> optional=adminDao.findByUsername(username);
		if(!optional.isPresent()) {
			return false;
		}
		optional.get().setActive(!optional.get().isActive());
		adminDao.saveUser(optional.get());
		return true;
	}

	@Override
	public Optional<List<UserVO>> getAllUsersByActivityStatus(boolean status) {
		log.info("AdminService: Inside getAllUSersByActivityStatus");
		Optional<List<UserEntity>> listEntity=adminDao.findUsersByStatus(status);
		log.info(listEntity.get().toString());
		List<UserVO> listVO=listEntity.get().stream()
				.map(entity->mapper.map(entity, UserVO.class)).toList();;
				
		return Optional.of(listVO);
	}

	@Override
	public boolean deleteUser(String username) {
		log.info("AdminService: Inside deleteUser");
		Optional<UserEntity> optional=adminDao.findByUsername(username);
		if(!optional.isPresent())return false;
		adminDao.deleteUser(optional.get().getUserId());
		return true;
	}
	
	@Override
	public Optional<RegisterResponseVO> createUserByAdmin(RegisterRequestAdmin registerDetails) {

	    log.info("AdminService: Inside createUserByAdmin");

	    Optional<UserEntity> optional = adminDao.findByUsername(registerDetails.getUsername());
	    if(optional.isPresent()) {
	        throw new UsernameNotAvailableException("Username Not Available!!");
	    }

	    Optional<UserEntity> optional1 = adminDao.findByEmail(registerDetails.getEmail());
	    if(optional1.isPresent()) {
	        throw new UsernameNotAvailableException("Email Not Available!!");
	    }

	    UserEntity newUser = mapper.map(registerDetails, UserEntity.class);

	    newUser.setUsername(registerDetails.getUsername().toLowerCase());
	    
	    newUser.setCreatedByAdmin(true);
	    newUser.setActive(true);
	    String loginPassword = generateLoginPassword();
	    String transactionPassword = generateTransactionPassword();

	    newUser.setLoginPassword(encoder.encode(loginPassword));
	    newUser.setTransactionPassword(encoder.encode(transactionPassword));

	    UserEntity addedUser = adminDao.save(newUser);

	    RegisterResponseVO response = mapper.map(addedUser, RegisterResponseVO.class);
	    response.setLoginPassword(loginPassword);
	    response.setTransactionPassword(transactionPassword);

	    log.info("User Created By Admin: {}", addedUser);

	    return Optional.of(response);
	}
	
	private String generateLoginPassword() {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    Random random = new Random();
	    StringBuilder password = new StringBuilder();

	    for(int i = 0; i < 8; i++) {
	        password.append(chars.charAt(random.nextInt(chars.length())));
	    }

	    return password.toString();
	}
	
	private String generateTransactionPassword() {
	    Random random = new Random();
	    StringBuilder password = new StringBuilder();

	    for(int i = 0; i < 6; i++) {
	        password.append(random.nextInt(10));
	    }

	    return password.toString();
	}
}

package com.rohan.service;

import java.util.List;
import java.util.Optional;

import com.rohan.model.RegisterRequestAdmin;
import com.rohan.vo.RegisterResponseVO;
import com.rohan.vo.UserVO;

public interface AdminService {

	Optional<List<UserVO>> getAllUsers();
	boolean changeUserStatus(String username);
	Optional<List<UserVO>> getAllUsersByActivityStatus(boolean status);
	boolean deleteUser(String username);
	Optional<RegisterResponseVO> createUserByAdmin(RegisterRequestAdmin registerDetails);
}
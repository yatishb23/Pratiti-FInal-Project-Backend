package com.rohan.dao;

import java.util.List;
import java.util.Optional;

import com.rohan.entity.UserEntity;

public interface AdminDao {

	List<UserEntity> getAllUsers();

	Optional<UserEntity> findByUsername(String username);

	Optional<List<UserEntity>> findUsersByStatus(boolean status);

	void saveUser(UserEntity user);
	
	UserEntity save(UserEntity user);

	void deleteUser(Long userId);

	Optional<UserEntity> findByEmail(String email);

}
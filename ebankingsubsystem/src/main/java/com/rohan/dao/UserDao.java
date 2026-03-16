package com.rohan.dao;

import java.util.Optional;

import com.rohan.entity.UserEntity;

public interface UserDao {

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findById(Long userId);

	Optional<UserEntity> findByEmail(String email);

	UserEntity save(UserEntity user);

}
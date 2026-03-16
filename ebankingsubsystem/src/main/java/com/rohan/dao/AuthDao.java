package com.rohan.dao;

import java.util.Optional;

import com.rohan.entity.UserEntity;

public interface AuthDao {

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findByEmail(String email);

	UserEntity save(UserEntity user);

}
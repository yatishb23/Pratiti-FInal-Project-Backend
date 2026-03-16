package com.rohan.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.rohan.entity.UserEntity;
import com.rohan.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
	private final UserRepository repository;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        return repository.findById(userId);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return repository.save(user);
    }
}

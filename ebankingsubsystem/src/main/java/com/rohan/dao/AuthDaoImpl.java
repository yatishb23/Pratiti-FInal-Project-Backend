package com.rohan.dao;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rohan.entity.UserEntity;
import com.rohan.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthDaoImpl implements AuthDao {

    private final UserRepository repository;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return repository.findByUsername(username);
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

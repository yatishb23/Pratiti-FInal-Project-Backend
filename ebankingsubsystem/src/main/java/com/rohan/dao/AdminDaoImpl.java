package com.rohan.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.rohan.entity.UserEntity;
import com.rohan.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AdminDaoImpl implements AdminDao {
	private final UserRepository repository;

    @Override
    public List<UserEntity> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<List<UserEntity>> findUsersByStatus(boolean status) {
        return repository.findAllByIsActive(status);
    }

    @Override
    public void saveUser(UserEntity user) {
        repository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        repository.deleteById(userId);
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

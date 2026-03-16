package com.rohan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rohan.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findByUsername(String username);
	Optional<List<UserEntity>> findAllByIsActive(boolean active);
	Optional<UserEntity> findByEmail(String username);
}

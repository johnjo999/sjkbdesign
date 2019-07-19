package com.sjkb.repositores;

import java.util.Optional;

import com.sjkb.entities.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

} 
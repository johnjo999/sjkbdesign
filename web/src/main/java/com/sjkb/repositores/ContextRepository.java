package com.sjkb.repositores;

import com.sjkb.entities.ContextEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContextRepository extends JpaRepository<ContextEntity, String> {
    
}
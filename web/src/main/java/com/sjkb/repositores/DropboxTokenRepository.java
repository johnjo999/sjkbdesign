package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.DropboxTokenEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DropboxTokenRepository extends JpaRepository<DropboxTokenEntity, Long> {

    List<DropboxTokenEntity>findAll();

    List<DropboxTokenEntity> findByUser(String user);
    
}
package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.DropboxTokenEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DropboxTokenRepository extends JpaRepository<DropboxTokenEntity, Long> {

    List<DropboxTokenEntity>findAll();

    DropboxTokenEntity findByUser(String user);

    List<DropboxTokenEntity> findByContext(String context);
    
}
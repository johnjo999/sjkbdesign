package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.JobEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobEntity, String> {

    List<JobEntity> findAllByUser(String empid);
    
    List<JobEntity> findByUser(String user);
    
}
package com.sjkb.repositores;

import com.sjkb.entities.JobExpenseEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobExpenseRepository extends JpaRepository<JobExpenseEntity, String> {
    
}
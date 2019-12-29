package com.sjkb.repositores;

import com.sjkb.entities.JobPaymentEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPaymentRepository extends JpaRepository<JobPaymentEntity, String> {
}
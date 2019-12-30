package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.InvoiceEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

	List<InvoiceEntity> findByCustomerId(String jobid);
}
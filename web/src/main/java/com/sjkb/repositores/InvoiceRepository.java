package com.sjkb.repositores;

import com.sjkb.entities.InvoiceEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
}
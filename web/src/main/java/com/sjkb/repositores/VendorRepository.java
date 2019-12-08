package com.sjkb.repositores;

import com.sjkb.entities.VendorEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<VendorEntity, String> {

    public VendorEntity findByRepId(String repId);

	public VendorEntity findByAccountId(String accountId);
}
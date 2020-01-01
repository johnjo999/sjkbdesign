package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.VendorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VendorRepository extends JpaRepository<VendorEntity, String> {

    public VendorEntity findByRepId(String repId);

	public VendorEntity findByAccountId(String accountId);

	public VendorEntity findByName(String name);

	@Query("SELECT v.name as name, u.uid as uid, v.multiplier as multiplier FROM contact u, vendor v WHERE u.uid = v.repId and u.context = :context and u.role = 'salesRep'")
	public List<CompanyRepsInterface> findByContext(@Param("context") String context);

	public List<VendorEntity> findByContextAndCabVendorTrue(String context);
	
	
}
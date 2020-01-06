package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.JobExpenseEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobExpenseRepository extends JpaRepository<JobExpenseEntity, String> {

	List<JobExpenseEntity> findByFolder(String jobid);
	// TODO:  reference company directly from vendor UID rather than via the rep's contact ID
	@Query("SELECT v.name as companyName, x.invoice as cost, x.uid as uid FROM expense x, vendor v WHERE x.folder = :folder and x.invoiced = false and v.repId = x.companyContactId")
	public List<JobExpenseInvoiceInterface> findByFolderAndNotInvoiced(@Param("folder") String folder);
    
}
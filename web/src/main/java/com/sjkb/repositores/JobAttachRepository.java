package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.JobAttachEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobAttachRepository extends JpaRepository<JobAttachEntity, String> {

	List<JobAttachEntity> findAllByJobidAndBinIsFalse(String jobid);

	List<JobAttachEntity> findByJobidAndPubIsTrue(String jobid);

}
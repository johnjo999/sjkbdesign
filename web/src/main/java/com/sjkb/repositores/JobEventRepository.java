package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.JobEventEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobEventRepository extends JpaRepository<JobEventEntity, String> {

	List<JobEventEntity> findAllByJobid(String jobid);

	List<JobEventEntity> findAllByJobidOrderByTimestamp(String jobid);

	List<JobEventEntity> findAllByJobidAndTypeOrderByTimestamp(String jobid, String role);

}
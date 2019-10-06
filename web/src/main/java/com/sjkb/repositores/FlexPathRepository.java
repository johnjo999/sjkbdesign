package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.FlexPathEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlexPathRepository extends JpaRepository<FlexPathEntity, Long> {

	List<FlexPathEntity> findByContextAndDepth(String context, int depth);

	List<FlexPathEntity> findByParent(Long iid);

	List<FlexPathEntity> findByContext(String context);
    
}
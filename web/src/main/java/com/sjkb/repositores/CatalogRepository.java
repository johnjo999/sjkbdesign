package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.CatalogEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<CatalogEntity, Long>  {

	List<CatalogEntity> findByContext(String context);

	List<CatalogEntity> findByContextAndItemId(String context, String category);

	CatalogEntity findByDefaultFlexPath(Long id);


}

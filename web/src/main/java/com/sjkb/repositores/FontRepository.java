package com.sjkb.repositores;

import java.util.List;

import com.sjkb.entities.FontFamilyEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FontRepository extends JpaRepository<FontFamilyEntity, String> {

	List<FontFamilyEntity> findAll();

	List<FontFamilyEntity> findByHomepageTrue();

}
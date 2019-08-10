package com.sjkb.repositores;

import com.sjkb.entities.HomePageStoryEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeStoryRepository extends JpaRepository<HomePageStoryEntity, Long> {

}
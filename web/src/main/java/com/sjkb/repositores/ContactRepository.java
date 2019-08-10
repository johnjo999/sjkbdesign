package com.sjkb.repositores;

import java.util.List;
import com.sjkb.entities.ContactEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactEntity, String>  {

    List<ContactEntity>findAll();

    ContactEntity findByUsername(String username);


}
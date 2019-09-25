package com.sjkb.repositores;

import java.util.List;
import com.sjkb.entities.ContactEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactEntity, String>  {

    List<ContactEntity>findByContext(String context);

    List<ContactEntity>findByContextOrderByLastname(String context);

    ContactEntity findByUsername(String username);

    ContactEntity findByUid(String uid);

	List<ContactEntity> findByRoleAndContext(String string, String context);


}
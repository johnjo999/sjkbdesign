package com.sjkb.repositores;

import java.util.List;
import com.sjkb.entities.ContactEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<ContactEntity, String>  {

    List<ContactEntity>findByContext(String context);

    List<ContactEntity>findByContextAndRole(String context, String role);

    List<ContactEntity>findByContextAndRoleOrderByFirstname(String context, String role);

    ContactEntity findByUsername(String username);

    ContactEntity findByUid(String uid);

    @Query("SELECT u FROM contact u WHERE u.company != 'NULL' and u.context = :context and u.role = 'salesRep'")
    List<CompanyRepsInterface> findCompanyByContext(@Param("context") String context);

    @Query("SELECT company FROM contact WHERE uid = :uid")
    String getCompanyForId(@Param("uid") String companyContactId);


}
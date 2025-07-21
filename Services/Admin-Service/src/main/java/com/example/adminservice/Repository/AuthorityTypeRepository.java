package com.example.adminservice.Repository;

import com.example.adminservice.Entity.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityTypeRepository extends JpaRepository<AuthorityType, Long>, JpaSpecificationExecutor<AuthorityType> {
    long count();
}

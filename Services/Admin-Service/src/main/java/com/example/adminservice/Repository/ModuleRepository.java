package com.example.adminservice.Repository;

import com.example.adminservice.Entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {
    Optional<Module> findByModuleCode(String code);
    long count();

}

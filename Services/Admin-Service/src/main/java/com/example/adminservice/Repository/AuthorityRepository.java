package com.example.adminservice.Repository;

import com.example.adminservice.Entity.Authority;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long>, JpaSpecificationExecutor<Authority> {
    List<Authority> getAuthoritiesByModule(Module module);

    List<Authority> getAuthoritiesByRoles(Role role);

    List<Authority> findByModuleIn(List<Module> modules);

    Optional<Authority> findByLibelleAndModule(String authorityLibelle, Module module);
    long count();
    long countByModule(Module module);

}

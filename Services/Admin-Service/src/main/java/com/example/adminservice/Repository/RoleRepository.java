package com.example.adminservice.Repository;

import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role ,Long>, JpaSpecificationExecutor<Role> {
    List<Role> findRolesByModule(Module module);
    List<Role> findByModuleIn(List<Module> modules);
    long count();
    long countByModule(Module module);
}

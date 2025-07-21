package com.example.adminservice.Repository;

import com.example.adminservice.Entity.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long>, JpaSpecificationExecutor<Groupe> {
    Optional<Groupe> findByLibelle(String libelle);
    long count();

}

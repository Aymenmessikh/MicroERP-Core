package com.example.adminservice.Repository;

import com.example.adminservice.Entity.ProfileAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileAuthorityRepository extends JpaRepository<ProfileAuthority, Long>, JpaSpecificationExecutor<ProfileAuthority> {
    Optional<ProfileAuthority> findByProfileIdAndAuthorityId(Long profileId, Long authorityId);
}

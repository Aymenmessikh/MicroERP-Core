package com.example.adminservice.Repository;

import com.example.adminservice.Entity.Groupe;
import com.example.adminservice.Entity.Module;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.ProfileAuthority;
import com.example.adminservice.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    List<Profile> findByUser(User user);

    Profile findByProfileAuthorities(ProfileAuthority profileAuthority);

    List<Profile> findAllByGroupe(Groupe groupe);
    long count();
//    long countByModule(Module module);


}

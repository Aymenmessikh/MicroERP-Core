package com.example.adminservice.Repository;
import com.example.adminservice.Dto.User.UserProfileInfoDto;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    Optional<User> findByProfiles(Profile profile);

    Optional<User> findByUserName(String userName);

    boolean existsByUserName(@NotNull(message = "User Name cannot be null") String userName);

    boolean existsByUuid(String uuid);

    boolean existsByEmail(@NotNull(message = "Email cannot be null") String email);
    long count();
    long countByActifTrue();
    @Query("SELECT u.id AS id ,u.firstName AS firstName, u.lastName AS lastName," +
            " u.email AS email, u.phoneNumber AS phoneNumber, p.libelle AS libelle " +
            "FROM User u JOIN u.profiles p JOIN p.modules m " +
            "WHERE m.id = :moduleId")
    List<UserProfileInfoDto> findUsersByModuleId(@Param("moduleId") Long moduleId);
}



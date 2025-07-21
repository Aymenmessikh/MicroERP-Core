package com.example.adminservice.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @NotBlank
    @Column(unique = true,nullable = false)
    private String userName;
    @Column(nullable = false,unique = true)
    private String email;
    @NotBlank
    @Column(nullable = false,unique = true)
    private String uuid;
    @Column(nullable = false,unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private Boolean actif;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Profile> profiles;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "actif_profile_id" , referencedColumnName = "id")
    private Profile actifProfile;

    public void addProfile(Profile profile) {
        if (this.getProfiles() != null && !this.getProfiles().isEmpty()) {
            this.getProfiles().add(profile);
            profile.setUser(this);
        }
    }
    public void removeProfile(Profile profile){
        if (this.getProfiles()!=null && !this.getProfiles().isEmpty()){
            this.getProfiles().remove(profile);
        }
    }
}

package com.example.adminservice.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Profile implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String libelle;
    @Column(nullable = false)
    private Boolean actif;
    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "groupe_id" , referencedColumnName = "id")
    private Groupe groupe;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "profile_role",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "profile_module",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private List<Module> modules;
    @OneToMany(mappedBy = "profile",fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private List<ProfileAuthority> profileAuthorities=new ArrayList<>();
    public void addRole(Role role){
        this.getRoles().add(role);
    }
    public void removeRole(Role role) {
        this.getRoles().remove(role);
    }
    public void addModule(Module module){
        this.getModules().add(module);
    }
    public void removeModule(Module module){
        this.getModules().remove(module);
    }
    public void addAuthority(ProfileAuthority profileAuthority){
        this.profileAuthorities.add(profileAuthority);
    }
    public void removeAuthority(ProfileAuthority profileAuthority){
        this.profileAuthorities.remove(profileAuthority);
    }
}

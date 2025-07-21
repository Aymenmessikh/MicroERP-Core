package com.example.adminservice.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true,nullable = false)
    private String libelle;
    @Column(nullable = false)
    private Boolean actif=Boolean.TRUE;
    @ManyToMany
    @JoinTable(
            name = "role_authority",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private List<Authority> authoritys;
    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id",nullable = false)
    private Module module;
    public void grantAuthority(Authority authority){
        this.authoritys.add(authority);
    }
    public void revokeAuthority(Authority authority){
        this.authoritys.remove(authority);
    }
}

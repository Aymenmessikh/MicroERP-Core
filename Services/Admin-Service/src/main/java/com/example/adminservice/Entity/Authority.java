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
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true,nullable = false)
    private String libelle;
    @Column(nullable = false)
    private Boolean actif;
    @ManyToOne
    @JoinColumn(name = "module_id", referencedColumnName = "id",nullable = false)
    private Module module;
    @ManyToOne
    @JoinColumn(name = "authority_Type_id", referencedColumnName = "id" , nullable = false)
    private AuthorityType authorityType;
    @ManyToMany(mappedBy = "authoritys")
    private List<Role> roles;
}

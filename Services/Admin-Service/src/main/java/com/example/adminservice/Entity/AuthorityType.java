package com.example.adminservice.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AuthorityType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true,nullable = false)
    private String libelle;
    @Column(nullable = false)
    private Boolean actif;
//    @OneToMany(mappedBy = "authorityType")
//    private List<Authority> authority;
}

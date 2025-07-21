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
public class Module implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(unique = true,nullable = false)
    private String moduleName;
    @NotBlank
    @Column(unique = true,nullable = false)
    private String moduleCode;
    @NotBlank
    @Column(nullable = false)
    private String color;
    @NotBlank
    @Column(nullable = false)
    private String uri;
    @NotBlank
    @Column(nullable = false)
    private String icon;
    @Column(nullable = false)
    private Boolean actif;
//    @OneToMany(mappedBy = "module")
//    private List<Role> roles;
//    @OneToMany(mappedBy = "module")
//    private List<Authority> authorities;
//    @ManyToMany(mappedBy = "modules")
//    private List<Profile> profiles;
}

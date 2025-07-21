package com.example.adminservice.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ProfileAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Boolean granted;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authority authority;
}

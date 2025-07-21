package com.example.adminservice.Dto.Profile;

import com.example.adminservice.Dto.Authority.AuthorityResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileAuthorityResponse {
    private Long id;
    private AuthorityResponse authorityResponse;
    private Boolean granted;
}

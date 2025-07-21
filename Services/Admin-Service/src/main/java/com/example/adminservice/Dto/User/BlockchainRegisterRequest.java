package com.example.adminservice.Dto.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockchainRegisterRequest {
    private String address;
    private String username;
}

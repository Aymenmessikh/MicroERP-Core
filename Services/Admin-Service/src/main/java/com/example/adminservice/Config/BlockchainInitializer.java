package com.example.adminservice.Config;

import com.example.adminservice.Services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class BlockchainInitializer {
    private final UserService userService;

    public BlockchainInitializer(UserService userService) {
        this.userService=userService;
    }

    @PostConstruct
    public void init() {
        userService.registerOnBlockchain("0x02f08Aa1D79e4897b6c063a8E7602219802B06cA", "admin");
    }
}

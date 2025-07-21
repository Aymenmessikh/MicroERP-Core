package com.example.adminservice.Controller;

import com.example.adminservice.Config.AuditLog.AuditEvent;
import com.example.adminservice.Dto.Authority.AuthorityResponse;
import com.example.adminservice.Dto.User.UserResponse;
import com.example.adminservice.Entity.Authority;
import com.example.adminservice.Entity.Profile;
import com.example.adminservice.Entity.User;
import com.example.adminservice.Services.AuthorityService;
import com.example.adminservice.Services.ExtracteAuthorityService;
import com.example.adminservice.Services.ProfileService;
import com.example.adminservice.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("test")
public class TestController {
    private final ExtracteAuthorityService extracteAuthorityService;
    private final UserService p;
    private final AuthorityService authorityService;
    @GetMapping("{id}")
    public ResponseEntity<Profile> get(@PathVariable Long id){
        Profile grantedAuthorities=extracteAuthorityService.fetchProfileWithAuthorities(id);
        return new ResponseEntity<>(grantedAuthorities, HttpStatus.OK);
    }
    @GetMapping("/auth/{id}")
    public ResponseEntity<Set<GrantedAuthority>> getauth(@PathVariable Long id){
        Set<GrantedAuthority> grantedAuthorities=extracteAuthorityService.extractAuthority(id);
        return new ResponseEntity<>(grantedAuthorities, HttpStatus.OK);
    }
    @GetMapping("u/{id}")
    public ResponseEntity<UserResponse> getuser(@PathVariable Long id){
        UserResponse grantedAuthorities=p.getUserById(id);
        return new ResponseEntity<>(grantedAuthorities, HttpStatus.OK);
    }

//    @GetMapping("getjwtFR")
//    public ResponseEntity<Object> getjwtFR(){
//        Object o=auditAspect.logRequest();
//        return new ResponseEntity<>(o, HttpStatus.OK);
//    }
}

package com.example.adminservice.Controller;

import com.example.adminservice.Config.filter.clause.Clause;
import com.example.adminservice.Config.filter.clause.ClauseOneArg;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.Critiria;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SearchValue;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SortParam;
import com.example.adminservice.Dto.Authority.AuthorityRequest;
import com.example.adminservice.Dto.Authority.AuthorityResponse;
import com.example.adminservice.Services.AuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/authority")
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;

    @PreAuthorize("hasAuthority('CREATE_AUTHORITY')")
    @PostMapping
    public ResponseEntity<AuthorityResponse> createAuthority(@Valid @RequestBody AuthorityRequest authorityRequest) {
        AuthorityResponse authorityResponse = authorityService.createAuthority(authorityRequest);
        return new ResponseEntity<>(authorityResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('READ_AUTHORITIES')")
    @GetMapping
    public ResponseEntity<PageImpl<AuthorityResponse>> getAllAuthorityss(@Critiria List<Clause> filter,
                                                                         @SearchValue ClauseOneArg searchValue,
                                                                         @SortParam PageRequest pageRequest) {
        filter.add(searchValue);
        PageImpl<AuthorityResponse> authorityResponses = authorityService.getAllAuthoritys(filter,pageRequest);
        return new ResponseEntity<>(authorityResponses, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('READ_AUTHORITY_BY_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorityResponse> getAuthorityById(@PathVariable Long id) {
        AuthorityResponse authorityResponse = authorityService.getAuthorityById(id);
        return new ResponseEntity<>(authorityResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_AUTHORITIES_BY_MODULE')")
    @GetMapping("/byModule/{id}")
    public ResponseEntity<List<AuthorityResponse>> getAllAuthoritiesByModule(@PathVariable Long id) {
        List<AuthorityResponse> authorityResponses = authorityService.getAllAuthorityByModule(id);
        return new ResponseEntity<>(authorityResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_AUTHORITIES_BY_ROLE')")
    @GetMapping("/byRole/{id}")
    public ResponseEntity<List<AuthorityResponse>> getAllAuthoritiesByRole(@PathVariable Long id) {
        List<AuthorityResponse> authorityResponses = authorityService.getAllAuthorityByRole(id);
        return new ResponseEntity<>(authorityResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_AUTHORITY')")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorityResponse> updateAuthority(@Valid @RequestBody AuthorityRequest authorityRequest,
                                                             @PathVariable Long id) {
        AuthorityResponse authorityResponse = authorityService.updateAuthority(authorityRequest, id);
        return new ResponseEntity<>(authorityResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ENABLE_AUTHORITY')")
    @PutMapping("/enable/{id}")
    public ResponseEntity<AuthorityResponse> enableAuthority(@PathVariable Long id) {
        AuthorityResponse authorityResponse = authorityService.enableAuthority(id);
        return new ResponseEntity<>(authorityResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_AUTHORITY')")
    @PutMapping("/disable/{id}")
    public ResponseEntity<AuthorityResponse> disableAuthority(@PathVariable Long id) {
        AuthorityResponse authorityResponse = authorityService.disableAuthority(id);
        return new ResponseEntity<>(authorityResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthority(@PathVariable Long id) {
        authorityService.deleteAuthority(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/ModuleAuthoritiesExcludingRoleAuthorities/{idRole}/{idModule}")
    public ResponseEntity<List<AuthorityResponse>> getModuleAuthoritiesExcludingRoleAuthorities
            (@PathVariable Long idRole,@PathVariable Long idModule) {
        List<AuthorityResponse> authorityResponses=authorityService.getModuleAuthoritiesExcludingRoleAuthorities(idRole,idModule);
        return new ResponseEntity<>(authorityResponses, HttpStatus.OK);
    }
    @GetMapping("/ModulesAuthoritiesExcludingProfileAuthorities/{idProfile}")
    public ResponseEntity<List<AuthorityResponse>> getModulesAuthoritiesExcludingProfileAuthorities(@PathVariable Long idProfile) {
        List<AuthorityResponse> authorityResponses=authorityService.getModulesAuthoritiesExcludingProfileAuthorities(idProfile);
        return new ResponseEntity<>(authorityResponses, HttpStatus.OK);
    }
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        Long count= authorityService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    @GetMapping("countByModule")
    public ResponseEntity<Long> countByModule(@RequestParam Long id) {
        Long count= authorityService.countByModule(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}

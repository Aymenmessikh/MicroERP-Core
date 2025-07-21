package com.example.adminservice.Controller;

import com.example.adminservice.Config.filter.clause.Clause;
import com.example.adminservice.Config.filter.clause.ClauseOneArg;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.Critiria;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SearchValue;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SortParam;
import com.example.adminservice.Dto.Authority.AuthorityResponse;
import com.example.adminservice.Dto.Role.RoleRequest;
import com.example.adminservice.Dto.Role.RoleResponse;
import com.example.adminservice.Services.RoleService;
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
@RequestMapping("api/v1/admin/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleRequest roleRequest){
        RoleResponse roleResponse = roleService.createRole(roleRequest);
        return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('READ_ALL_ROLES')")
    @GetMapping
    public ResponseEntity<PageImpl<RoleResponse>> getAllAuthorityss(@Critiria List<Clause> filter,
                                                                    @SearchValue ClauseOneArg searchValue,
                                                                    @SortParam PageRequest pageRequest) {
        filter.add(searchValue);
        PageImpl<RoleResponse> roleResponses = roleService.getAllRoles(filter,pageRequest);
        return new ResponseEntity<>(roleResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ROLE')")
    @GetMapping("{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id){
        RoleResponse roleResponse = roleService.getRoleById(id);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_ROLES_BY_MODULE')")
    @GetMapping("byModule/{id}")
    public ResponseEntity<List<RoleResponse>> getAllRoleByModule(@PathVariable Long id){
        List<RoleResponse> roleResponses = roleService.getAllRoleByModule(id);
        return new ResponseEntity<>(roleResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(@RequestBody RoleRequest roleRequest, @PathVariable Long id){
        RoleResponse roleResponse = roleService.updateRole(roleRequest, id);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ENABLE_ROLE')")
    @PutMapping("/enable/{id}")
    public ResponseEntity<RoleResponse> enableRole(@PathVariable Long id){
        RoleResponse roleResponse = roleService.enableRole(id);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_ROLE')")
    @PutMapping("/disable/{id}")
    public ResponseEntity<RoleResponse> disableRole(@PathVariable Long id){
        RoleResponse roleResponse = roleService.disableRole(id);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
    }

    @PreAuthorize("hasAuthority('GRANT_AUTHORITY_TO_ROLE')")
    @PostMapping("grantAuth/{roleId}")
    public ResponseEntity<RoleResponse> grantAuthoritiesToRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> authorityIds) {
        RoleResponse roleResponse = roleService.grantAuthoritiesToRole(roleId, authorityIds);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('REVOKE_AUTHORITY_FROM_ROLE')")
    @DeleteMapping("revokeAuth/{roleId}")
    public ResponseEntity<RoleResponse> revokeAuthoritiesFromRole(
            @PathVariable Long roleId,
            @RequestBody List<Long> authorityIds) {
        RoleResponse roleResponse = roleService.revokeAuthoritiesFromRole(roleId, authorityIds);
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }
    @GetMapping("/mdulesRolesExcludingProfileRole/{idProfile}")
    public ResponseEntity<List<RoleResponse>> getModulesRoleExcludingProfileRoles(@PathVariable Long idProfile) {
        List<RoleResponse> roleResponses=roleService.getModulesRolesExcludingProfileRole(idProfile);
        return new ResponseEntity<>(roleResponses, HttpStatus.OK);
    }
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        Long count= roleService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    @GetMapping("countByModule")
    public ResponseEntity<Long> countByModule(@RequestParam Long id) {
        Long count= roleService.countByModule(id);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

}

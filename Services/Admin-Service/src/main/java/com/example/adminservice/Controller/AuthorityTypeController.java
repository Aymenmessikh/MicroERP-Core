package com.example.adminservice.Controller;

import com.example.adminservice.Dto.AuthorityType.AuthorityTypeRequest;
import com.example.adminservice.Dto.AuthorityType.AuthorityTypeResponse;
import com.example.adminservice.Services.AuthorityTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/authorityType")
@RequiredArgsConstructor
public class AuthorityTypeController {

    private final AuthorityTypeService authorityTypeService;

    @PreAuthorize("hasAuthority('CREATE_AUTHORITY_TYPE')")
    @PostMapping
    public ResponseEntity<AuthorityTypeResponse> createAuthorityType(@Valid @RequestBody AuthorityTypeRequest authorityTypeRequest) {
        AuthorityTypeResponse authorityTypeResponse = authorityTypeService.createAuthorityType(authorityTypeRequest);
        return new ResponseEntity<>(authorityTypeResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('READ_AUTHORITY_TYPES')")
    @GetMapping
    public ResponseEntity<List<AuthorityTypeResponse>> getAllAuthorityTypes() {
        List<AuthorityTypeResponse> authorityTypeResponses = authorityTypeService.getAllAuthoritysType();
        return new ResponseEntity<>(authorityTypeResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_AUTHORITY_TYPE_BY_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorityTypeResponse> getAuthorityTypeById(@PathVariable Long id) {
        AuthorityTypeResponse authorityTypeResponse = authorityTypeService.getAuthorityTypeById(id);
        return new ResponseEntity<>(authorityTypeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_AUTHORITY_TYPE')")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorityTypeResponse> updateAuthorityType(@Valid @RequestBody AuthorityTypeRequest authorityTypeRequest,
                                                                     @PathVariable Long id) {
        AuthorityTypeResponse authorityTypeResponse = authorityTypeService.updateAuthorityType(authorityTypeRequest, id);
        return new ResponseEntity<>(authorityTypeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ENABLE_AUTHORITY_TYPE')")
    @PutMapping("/enable/{id}")
    public ResponseEntity<AuthorityTypeResponse> enableAuthorityType(@PathVariable Long id) {
        AuthorityTypeResponse authorityTypeResponse = authorityTypeService.enableAuthorityType(id);
        return new ResponseEntity<>(authorityTypeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_AUTHORITY_TYPE')")
    @PutMapping("/disable/{id}")
    public ResponseEntity<AuthorityTypeResponse> disableAuthorityType(@PathVariable Long id) {
        AuthorityTypeResponse authorityTypeResponse = authorityTypeService.disableAuthorityType(id);
        return new ResponseEntity<>(authorityTypeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DELETE_AUTHORITY_TYPE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorityType(@PathVariable Long id) {
        authorityTypeService.deleteAuthorityType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        Long count= authorityTypeService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}

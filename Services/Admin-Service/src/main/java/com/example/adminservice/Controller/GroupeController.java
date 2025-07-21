package com.example.adminservice.Controller;

import com.example.adminservice.Config.filter.clause.Clause;
import com.example.adminservice.Config.filter.clause.ClauseOneArg;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.Critiria;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SearchValue;
import com.example.adminservice.Config.filter.handlerMethodeArgumentResolver.SortParam;
import com.example.adminservice.Dto.Groupe.GroupeRequest;
import com.example.adminservice.Dto.Groupe.GroupeResponse;
import com.example.adminservice.Services.GroupeService;
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
@RequestMapping("api/v1/admin/groupe")
@RequiredArgsConstructor
public class GroupeController {

    private final GroupeService groupeService;

    @PreAuthorize("hasAuthority('CREATE_GROUPE')")
    @PostMapping
    public ResponseEntity<GroupeResponse> createGroupe(@Valid @RequestBody GroupeRequest groupeRequest) {
        GroupeResponse groupeResponse = groupeService.createGroupe(groupeRequest);
        return new ResponseEntity<>(groupeResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('READ_GROUPES')")
    @GetMapping("get")
    public ResponseEntity<List<GroupeResponse>> getAllGroupes() {
        List<GroupeResponse> groupeResponses = groupeService.getAllGroupes();
        return new ResponseEntity<>(groupeResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_GROUPES')")
    @GetMapping
    public ResponseEntity<PageImpl<GroupeResponse>> getAllGroupes(@Critiria List<Clause> filter,
                                                                  @SearchValue ClauseOneArg searchValue,
                                                                  @SortParam PageRequest pageRequest) {
        filter.add(searchValue);
        PageImpl<GroupeResponse> groupeResponses = groupeService.getAllGroupesWithFilter(filter, pageRequest);
        return new ResponseEntity<>(groupeResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_GROUPE_BY_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<GroupeResponse> getGroupeById(@PathVariable Long id) {
        GroupeResponse groupeResponse = groupeService.getGroupeById(id);
        return new ResponseEntity<>(groupeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_GROUPE_BY_LIBELLE')")
    @GetMapping("/byLibelle/{libelle}")
    public ResponseEntity<GroupeResponse> getGroupeByLibelle(@PathVariable String libelle) {
        GroupeResponse groupeResponse = groupeService.getGroupeByLibelle(libelle);
        return new ResponseEntity<>(groupeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_GROUPE')")
    @PutMapping("/{id}")
    public ResponseEntity<GroupeResponse> updateGroupe(@Valid @RequestBody GroupeRequest groupeRequest, @PathVariable Long id) {
        GroupeResponse groupeResponse = groupeService.updateGroupe(groupeRequest, id);
        return new ResponseEntity<>(groupeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ENABLE_GROUPE')")
    @PutMapping("/enable/{id}")
    public ResponseEntity<GroupeResponse> enableGroupe(@PathVariable Long id) {
        GroupeResponse groupeResponse = groupeService.enableGroupe(id);
        return new ResponseEntity<>(groupeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_GROUPE')")
    @PutMapping("/disable/{id}")
    public ResponseEntity<GroupeResponse> disableGroupe(@PathVariable Long id) {
        GroupeResponse groupeResponse = groupeService.disableGroupe(id);
        return new ResponseEntity<>(groupeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DELETE_GROUPE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupe(@PathVariable Long id) {
        groupeService.deleteGroupe(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        Long count= groupeService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}

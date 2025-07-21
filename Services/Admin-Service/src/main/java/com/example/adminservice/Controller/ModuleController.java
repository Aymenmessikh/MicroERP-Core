package com.example.adminservice.Controller;

import com.example.adminservice.Dto.Module.ModuleRequest;
import com.example.adminservice.Dto.Module.ModuleResponse;
import com.example.adminservice.Services.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/module")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PreAuthorize("hasAuthority('CREATE_MODULE')")
    @PostMapping
    public ResponseEntity<ModuleResponse> createModule(@Valid @RequestBody ModuleRequest moduleRequest) {
        ModuleResponse moduleResponse = moduleService.createModule(moduleRequest);
        return new ResponseEntity<>(moduleResponse, HttpStatus.CREATED);
    }
    @PostMapping("/with-icon")
    public ResponseEntity<ModuleResponse> createModuleWithIcon(
            @RequestParam("moduleName") String moduleName,
            @RequestParam("moduleCode") String moduleCode,
            @RequestParam("uri") String uri,
            @RequestParam("color") String color,
            @RequestParam("icon") MultipartFile iconFile) {

        ModuleRequest moduleRequest = ModuleRequest.builder()
                .moduleName(moduleName)
                .moduleCode(moduleCode)
                .uri(uri)
                .color(color)
                .build();

        ModuleResponse response = moduleService.createModuleWithIcon(moduleRequest, iconFile);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('READ_MODULES')")
    @GetMapping
    public ResponseEntity<List<ModuleResponse>> getAllModules() {
        List<ModuleResponse> moduleResponses = moduleService.getAllModules();
        return new ResponseEntity<>(moduleResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_MODULE_BY_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<ModuleResponse> getModuleById(@PathVariable Long id) {
        ModuleResponse moduleResponse = moduleService.getModuleById(id);
        return new ResponseEntity<>(moduleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_MODULE_BY_CODE')")
    @GetMapping("/byCode/{code}")
    public ResponseEntity<ModuleResponse> getModuleByCode(@PathVariable String code) {
        ModuleResponse moduleResponse = moduleService.getModuleByCode(code);
        return new ResponseEntity<>(moduleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE_MODULE')")
    @PutMapping("/{id}")
    public ResponseEntity<ModuleResponse> updateModule(@Valid @RequestBody ModuleRequest moduleRequest, @PathVariable Long id) {
        ModuleResponse moduleResponse = moduleService.updateModule(moduleRequest, id);
        return new ResponseEntity<>(moduleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ENABLE_MODULE')")
    @PutMapping("/enable/{id}")
    public ResponseEntity<ModuleResponse> enableModule(@PathVariable Long id) {
        ModuleResponse moduleResponse = moduleService.enableModule(id);
        return new ResponseEntity<>(moduleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DISABLE_MODULE')")
    @PutMapping("/disable/{id}")
    public ResponseEntity<ModuleResponse> disableModule(@PathVariable Long id) {
        ModuleResponse moduleResponse = moduleService.disableModule(id);
        return new ResponseEntity<>(moduleResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DELETE_MODULE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/modulesExcludingModuleProfile/{idProfile}")
    public ResponseEntity<List<ModuleResponse>> getModulesExcludingModuleProfile(@PathVariable Long idProfile) {
        List<ModuleResponse> moduleResponses = moduleService.getModulesExcludingModuleProfile(idProfile);
        return new ResponseEntity<>(moduleResponses, HttpStatus.OK);
    }

    @GetMapping("/modulesByProfile/{username}")
    public ResponseEntity<List<ModuleResponse>> getModulesByProfile(@PathVariable String username) {
        List<ModuleResponse> moduleResponses = moduleService.getModulesByProfile(username);
        return new ResponseEntity<>(moduleResponses, HttpStatus.OK);
    }

    @GetMapping("count")
    public ResponseEntity<Long> count() {
        Long count = moduleService.count();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}

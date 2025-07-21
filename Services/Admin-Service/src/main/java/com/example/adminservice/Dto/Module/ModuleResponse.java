package com.example.adminservice.Dto.Module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleResponse {
    private Long id;
    private String moduleName;
    private String moduleCode;
    private String color;
    private String uri;
    private String icon;
    private Boolean actif;
}

package com.example.adminservice.Dto.Module;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleRequest {
    @NotNull(message = "Module Name cannot be null")
    private String moduleName;
    @NotNull(message = "Module Code cannot be null")
    private String moduleCode;
    @NotNull(message = "color cannot be null")
    private String color;
    @NotNull(message = "Uri cannot be null")
    private String uri;
    @NotNull(message = "Icon cannot be null")
    private String icon;

}

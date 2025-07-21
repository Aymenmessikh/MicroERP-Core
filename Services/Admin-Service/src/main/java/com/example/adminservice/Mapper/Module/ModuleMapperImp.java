package com.example.adminservice.Mapper.Module;

import com.example.adminservice.Dto.Module.ModuleRequest;
import com.example.adminservice.Dto.Module.ModuleResponse;
import com.example.adminservice.Entity.Module;
import org.springframework.stereotype.Component;

@Component
public class ModuleMapperImp implements ModuleMapper {
    @Override
    public Module EntityFromDto(ModuleRequest moduleRequest) {
        return Module.builder()
                .moduleName(moduleRequest.getModuleName())
                .moduleCode(moduleRequest.getModuleCode())
                .uri(moduleRequest.getUri())
                .color(moduleRequest.getColor())
                .icon(moduleRequest.getIcon())
                .actif(true)
                .build();
    }

    @Override
    public ModuleResponse DtoFromEntity(Module module) {
        return ModuleResponse.builder()
                .color(module.getColor())
                .icon(module.getIcon())
                .moduleCode(module.getModuleCode())
                .moduleName(module.getModuleName())
                .uri(module.getUri())
                .id(module.getId())
                .actif(module.getActif())
                .build();
    }
}

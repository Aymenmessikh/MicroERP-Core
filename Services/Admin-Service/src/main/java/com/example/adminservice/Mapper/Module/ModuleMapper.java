package com.example.adminservice.Mapper.Module;

import com.example.adminservice.Dto.Module.ModuleRequest;
import com.example.adminservice.Dto.Module.ModuleResponse;
import com.example.adminservice.Entity.Module;

public interface ModuleMapper {
    Module EntityFromDto(ModuleRequest moduleRequest);

    ModuleResponse DtoFromEntity(Module module);
}

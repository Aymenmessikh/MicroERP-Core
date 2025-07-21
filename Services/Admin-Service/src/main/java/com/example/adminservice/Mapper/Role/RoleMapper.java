package com.example.adminservice.Mapper.Role;

import com.example.adminservice.Dto.Role.RoleRequest;
import com.example.adminservice.Dto.Role.RoleResponse;
import com.example.adminservice.Entity.Role;

public interface RoleMapper {
    Role EntityFromDto(RoleRequest roleRequest);

    RoleResponse DtoFromEntity(Role role);
}

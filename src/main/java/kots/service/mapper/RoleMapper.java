package kots.service.mapper;

import kots.controller.dto.RoleDto;
import kots.model.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDto toRoleDtos(final Role role) {
        return new RoleDto(role.getId(), role.getName());
    }

    public static Set<RoleDto> toRoleDtos(final Set<Role> roles) {
        return roles.stream()
                .map(RoleMapper::toRoleDtos)
                .collect(Collectors.toSet());
    }
}

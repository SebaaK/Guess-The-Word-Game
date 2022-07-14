package kots.service.mapper;

import kots.controller.dto.RoleDto;
import kots.model.Role;

import java.util.Collection;
import java.util.List;

public class RoleMapper {

    public static RoleDto toRoleDtos(final Role role) {
        return new RoleDto(role.getId(), role.getName());
    }

    public static List<RoleDto> toRoleDtos(final Collection<Role> roles) {
        return roles.stream()
                .map(RoleMapper::toRoleDtos)
                .toList();
    }
}

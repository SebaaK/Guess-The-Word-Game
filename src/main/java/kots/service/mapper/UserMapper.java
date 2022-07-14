package kots.service.mapper;

import kots.controller.dto.UserDto;
import kots.model.User;

import static kots.service.mapper.RoleMapper.toRoleDtos;

public class UserMapper {

    public static UserDto toUserDto(final User user) {
        return new UserDto(user.getId(), user.getName(), RoleMapper.toRoleDtos(user.getRoles()));
    }
}

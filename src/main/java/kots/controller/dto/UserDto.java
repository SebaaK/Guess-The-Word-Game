package kots.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private Set<RoleDto> roleDto;
}

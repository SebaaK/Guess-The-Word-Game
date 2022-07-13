package kots.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private List<RoleDto> roleDto;
}

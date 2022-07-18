package kots.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class UserNameAndIdGameDto {

    private String userName;
    private String idGame;
}

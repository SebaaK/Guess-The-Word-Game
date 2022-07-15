package kots.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GameDto {

    private String id;
    private LocalDateTime started;
    private LocalDateTime finished;
    private String gameStatus;
    private WordMetadataDto word;
    private UserDto user;
}

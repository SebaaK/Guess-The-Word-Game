package kots.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GameUserDto {

    private String id;
    private LocalDateTime started;
    private LocalDateTime finished;
    private String gameStatus;
    private WordMetadataDto word;
    private List<Character> foundChars;
}

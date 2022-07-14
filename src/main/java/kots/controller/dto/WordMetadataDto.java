package kots.controller.dto;

import kots.domain.WordDifficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WordMetadataDto {

    private Long id;
    private String word;
    private WordDifficulty difficulty;
}

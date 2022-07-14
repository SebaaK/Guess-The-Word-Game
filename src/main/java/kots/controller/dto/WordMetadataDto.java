package kots.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WordMetadataDto {

    private String word;
    private String difficulty;
}

package kots.service.mapper;

import kots.controller.dto.WordMetadataDto;
import kots.model.Word;

import java.util.List;
import java.util.stream.StreamSupport;

public class WordMapper {

    public static WordMetadataDto toWordMetadataDto(final Word word) {
        return new WordMetadataDto(word.getId(), word.getWord(), word.getDifficulty());
    }

    public static List<WordMetadataDto> toWordMetadataDto(final Iterable<Word> wordList) {
        return StreamSupport.stream(wordList.spliterator(), false)
                .map(WordMapper::toWordMetadataDto)
                .toList();
    }
}

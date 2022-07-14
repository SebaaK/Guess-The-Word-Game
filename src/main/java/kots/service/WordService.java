package kots.service;

import kots.controller.dto.WordFileDownloadDto;
import kots.controller.dto.WordMetadataDto;
import kots.exception.NoFileException;
import kots.exception.ObjectNotFoundException;
import kots.exception.ProcessedFileException;
import kots.exception.WordNameIsExistException;
import kots.model.Word;
import kots.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static kots.service.mapper.WordMapper.toWordMetadataDto;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final WordChecker wordChecker;

    public WordMetadataDto store(MultipartFile file, String wordName) {
        checkWordNameIsFree(wordName);
        try {
            Word word = Word.builder()
                    .word(wordName)
                    .difficulty(wordChecker.getDifficulty(wordName))
                    .voice(checkIfWordFileExists(file).getBytes())
                    .build();
            return toWordMetadataDto(wordRepository.save(word));
        } catch (IOException e) {
            throw new ProcessedFileException("Cannot processed this file");
        }
    }

    public List<WordMetadataDto> getWords() {
        return toWordMetadataDto(wordRepository.findAll());
    }

    public WordFileDownloadDto getWord(String wordName) {
        Word word = getSingleWord(wordName);
        return new WordFileDownloadDto(word.getWord(), new ByteArrayResource(word.getVoice()));
    }

    public void deleteWord(String wordName) {
        wordRepository.delete(getSingleWord(wordName));
    }

    private void checkWordNameIsFree(String wordName) {
        if(wordRepository.existsWordByWord(wordName))
            throw new WordNameIsExistException("That word name is already exist");
    }

    private Word getSingleWord(String wordName) {
        return wordRepository.findByWord(wordName)
                .orElseThrow(() -> new ObjectNotFoundException("Word not found"));
    }

    private MultipartFile checkIfWordFileExists(MultipartFile file) {
        if(!file.isEmpty())
            return file;
        else
            throw new NoFileException("File not found!");
    }
}

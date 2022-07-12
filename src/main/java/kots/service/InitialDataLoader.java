package kots.service;

import kots.domain.WordDifficulty;
import kots.model.Word;
import kots.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
@Log4j2
class InitialData {

    @Value("${csvData.fileName:words.csv}")
    private String fileName;
    private final WordRepository wordRepository;

    @EventListener
    public void setUpData(ApplicationReadyEvent event) {
        List<Word> wordList = new ArrayList<>();
        try {
            File file = ResourceUtils.getFile("classpath:" + fileName);
            Scanner rowScanner = new Scanner(file);
            while(rowScanner.hasNext()) {
                wordList.add(completeEntityToSave(rowScanner.next().split(",")));
            }
            rowScanner.close();
        } catch (FileNotFoundException e) {
            log.warn("Initial data error service: " + e.getMessage());
        }
        wordRepository.saveAll(wordList);
    }

    private Word completeEntityToSave(String[] splitRow) {
        return new Word(Long.parseLong(splitRow[0]), splitRow[1], WordDifficulty.valueOf(splitRow[2]), splitRow[3].getBytes());
    }
}

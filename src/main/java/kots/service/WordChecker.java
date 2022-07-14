package kots.service;

import kots.model.WordDifficulty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WordChecker {

    @Value("${game.difficultyLevelThreshold:6}")
    private int difficultyLevelThreshold;

    public WordDifficulty getDifficulty(String word) {
        return word.length() <= difficultyLevelThreshold ? WordDifficulty.EASY : WordDifficulty.HARD;
    }
}

package kots.service;

import kots.domain.WordDifficulty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WordChecker {

    @Value("${game.difficultyLevelChanger:6}")
    private int difficultyLevelChanger;

    public WordDifficulty getDifficulty(String word) {
        return word.length() <= difficultyLevelChanger ? WordDifficulty.EASY : WordDifficulty.HARD;
    }
}

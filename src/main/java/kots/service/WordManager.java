package kots.service;

import kots.model.Word;
import kots.model.WordDifficulty;
import kots.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class WordManager {

    @Value("${game.difficultyLevelThreshold:6}")
    private int difficultyLevelThreshold;

    private final WordRepository wordRepository;

    public WordDifficulty getDifficulty(String word) {
        return word.length() <= difficultyLevelThreshold ? WordDifficulty.EASY : WordDifficulty.HARD;
    }

    public Word getRandomWord(String difficulty) {
        List<Word> wordsByDifficulty = wordRepository.findAllByDifficulty(parseStringToEnum(difficulty));
        return wordsByDifficulty.get(getRandom(wordsByDifficulty.size()));
    }

    public List<Character> initEmptyFoundCharsList(String word) {
        List<Character> characterList = new ArrayList<>();
        for(int i = 0; i < word.length(); i++) {
            characterList.add('_');
        }
        return characterList;
    }

    private WordDifficulty parseStringToEnum(String difficulty) {
        return WordDifficulty.valueOf(difficulty);
    }

    private int getRandom(int arraySize) {
        return new Random().nextInt(arraySize);
    }
}

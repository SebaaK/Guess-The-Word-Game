package kots.repository;

import kots.model.Word;
import kots.model.WordDifficulty;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WordRepository extends CrudRepository<Word, Long> {

    Optional<Word> findByName(String wordName);
    boolean existsWordByName(String word);
    List<Word> findAllByDifficulty(WordDifficulty difficulty);
}

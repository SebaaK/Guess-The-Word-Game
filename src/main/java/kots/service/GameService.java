package kots.service;

import kots.controller.dto.GameUserDto;
import kots.model.Game;
import kots.model.GameStatus;
import kots.model.Word;
import kots.repository.GameRepository;
import kots.service.mapper.GameMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final WordManager wordManager;
    private final UserService userService;

    public GameUserDto createNewGame(String userName, String difficulty) {
        Word randomWord = wordManager.getRandomWord(difficulty);
        Game game = Game.builder()
                .gameStatus(GameStatus.PLAY)
                .word(randomWord)
                .user(userService.getUser(userName))
                .foundChars(wordManager.initEmptyFoundCharsList(randomWord.getWord()))
                .build();
        return GameMapper.toGameDto(gameRepository.save(game));
    }
}

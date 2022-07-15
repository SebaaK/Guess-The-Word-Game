package kots.service;

import kots.controller.dto.GameDto;
import kots.model.Game;
import kots.model.GameStatus;
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

    public GameDto createNewGame(String userName, String difficulty) {
        Game game = Game.builder()
                .gameStatus(GameStatus.PLAY)
                .word(wordManager.getRandomWord(difficulty))
                .user(userService.getUser(userName))
                .build();
        return GameMapper.toGameDto(gameRepository.save(game));
    }
}

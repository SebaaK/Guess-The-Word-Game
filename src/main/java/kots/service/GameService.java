package kots.service;

import kots.controller.dto.GameUserDto;
import kots.controller.dto.ValidatedCharGameDto;
import kots.exception.CharNotValidPlaceException;
import kots.exception.ObjectNotFoundException;
import kots.model.Game;
import kots.model.GameStatus;
import kots.model.Word;
import kots.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static kots.service.mapper.GameMapper.toGameDto;

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
                .foundChars(wordManager.initEmptyFoundCharsList(randomWord.getName()))
                .build();
        return toGameDto(gameRepository.save(game));
    }

    public GameUserDto getGame(String userName, String idGame) {
        return toGameDto(findGame(userName, idGame));
    }

    @Transactional
    public GameUserDto validCharPlaceInWord(ValidatedCharGameDto validatedCharGameDto) {
        Game game = findGame(validatedCharGameDto.getUserName(), validatedCharGameDto.getIdGame());
        String wordName = game.getWord().getName();
        if(wordManager.charIsRightPlaceInWord(wordName, validatedCharGameDto)) {
            game.getFoundChars().set(validatedCharGameDto.getPlaceAt(), validatedCharGameDto.getCharOfWord());
            return toGameDto(game);
        } else
            throw new CharNotValidPlaceException("Char is not valid");
    }

    private Game findGame(String userName, String idGame) {
        return gameRepository.findByUserNameAndId(userName, idGame)
                .orElseThrow(() -> new ObjectNotFoundException("Game not found!"));
    }
}

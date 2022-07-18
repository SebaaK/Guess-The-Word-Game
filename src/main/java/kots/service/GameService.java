package kots.service;

import kots.controller.dto.GameUserDto;
import kots.controller.dto.UserNameAndIdGameDto;
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

import java.time.LocalDateTime;

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

    public GameUserDto getGame(UserNameAndIdGameDto userNameAndIdGameDto) {
        return toGameDto(findGame(userNameAndIdGameDto));
    }

    @Transactional
    public GameUserDto validCharPlaceInWord(ValidatedCharGameDto validatedCharGameDto) {
        Game game = findGame(UserNameAndIdGameDto.of(validatedCharGameDto.getUserName(), validatedCharGameDto.getIdGame()));
        String wordName = game.getWord().getName();
        if(wordManager.charIsRightPlaceInWord(wordName, validatedCharGameDto)) {
            game.getFoundChars().set(validatedCharGameDto.getPlaceAt(), validatedCharGameDto.getCharOfWord());
            checkCharsIsFillComplete(game);
            return toGameDto(game);
        } else
            throw new CharNotValidPlaceException("Char is not valid");
    }

    private void checkCharsIsFillComplete(Game game) {
        for(char charOfWord : game.getFoundChars()) {
            if(charOfWord == '_') {
                return;
            }
        }
        game.setFinished(LocalDateTime.now());
        game.setGameStatus(GameStatus.FINISH);
    }

    private Game findGame(UserNameAndIdGameDto userNameAndIdGameDto) {
        return gameRepository.findByUserNameAndId(userNameAndIdGameDto.getUserName(), userNameAndIdGameDto.getIdGame())
                .orElseThrow(() -> new ObjectNotFoundException("Game not found!"));
    }

    public void deleteGame(UserNameAndIdGameDto userNameAndIdGameDto) {
        gameRepository.delete(findGame(userNameAndIdGameDto));
    }
}

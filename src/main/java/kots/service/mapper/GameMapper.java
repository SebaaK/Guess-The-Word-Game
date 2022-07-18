package kots.service.mapper;

import kots.controller.dto.GameUserDto;
import kots.model.Game;

import static kots.service.mapper.WordMapper.toWordMetadataDto;

public class GameMapper {

    public static GameUserDto toGameDto(final Game game) {
        return new GameUserDto(
                game.getId(),
                game.getStarted(),
                game.getFinished(),
                game.getGameStatus().toString(),
                toWordMetadataDto(game.getWord()),
                game.getFoundChars()
        );
    }
}

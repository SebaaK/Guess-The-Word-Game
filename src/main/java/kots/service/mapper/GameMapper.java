package kots.service.mapper;

import kots.controller.dto.GameDto;
import kots.model.Game;

import static kots.service.mapper.UserMapper.toUserDto;
import static kots.service.mapper.WordMapper.toWordMetadataDto;

public class GameMapper {

    public static GameDto toGameDto(final Game game) {
        return new GameDto(
                game.getId(),
                game.getStarted(),
                game.getFinished(),
                game.getGameStatus().toString(),
                toWordMetadataDto(game.getWord()),
                toUserDto(game.getUser())
        );
    }
}

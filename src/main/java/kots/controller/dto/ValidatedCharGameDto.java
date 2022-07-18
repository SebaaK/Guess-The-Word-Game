package kots.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatedCharGameDto {

    private String userName;
    private String idGame;
    private char charOfWord;
    private int placeAt;

    public static ValidatedCharGameDto of(String userName, String idGame, char charOfWord, int placeAt) {
        return new ValidatedCharGameDto(userName, idGame, Character.toUpperCase(charOfWord), placeAt);
    }
}

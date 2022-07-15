package kots.controller;

import kots.controller.dto.GameDto;
import kots.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<GameDto> createNewGame(@AuthenticationPrincipal String userName,
                                                 @PathParam("difficulty") String difficulty) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createNewGame(userName, difficulty));
    }
}

package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.dto.GameStatisticDto;
import ru.krey.games.service.GameService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;

    @PostMapping("/byplayer")
    public List<GameStatisticDto> getGameStatistic(@RequestParam Long id, @RequestParam Long from, @RequestParam Long to) {
        List<Game> games = gameService.getPartOfGamesStatisticById(id, from, to);
        return games.stream().map((game)->{
            String gameResult = game.getTextGameResultByPlayerId(id);
            String entityName = game.getEntityNameByPlayerId(id);
            return GameStatisticDto.byInfo(game, entityName, gameResult);
        }).collect(Collectors.toList());
    }

    @GetMapping("/current/{player_id}/{game_code}")
    public ResponseEntity<?> getCurrentGameId(@PathVariable("player_id")  Long playerId, @PathVariable("game_code") Integer gameCode){
        return gameService.getCurrentGameId(playerId,gameCode);
    }
}

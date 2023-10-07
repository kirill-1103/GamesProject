package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}

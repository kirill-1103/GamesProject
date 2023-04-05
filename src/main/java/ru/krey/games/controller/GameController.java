package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.TttGame;
import ru.krey.games.dto.GameStatisticDto;
import ru.krey.games.utils.GameUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class GameController {
    private final TttGameDao tttGameDao;

    @PostMapping("/byplayer")
    public List<GameStatisticDto> getGameStatistic(@RequestParam Long id, @RequestParam Long from, @RequestParam Long to){
        List<GameStatisticDto> games = new ArrayList<>();

        List<GameStatisticDto> tttGames = getTttGames(id);

        if(tttGames.size() == 0){
            return tttGames;
        }

        games.addAll(tttGames);
        /* ... */

        games.sort((game1,game2)-> -game1.getTime().compareTo(game2.getTime()));

        return games.subList(Math.min(games.size()-1,from.intValue()),Math.min(games.size()-1,to.intValue()));
    }

    private List<GameStatisticDto> getTttGames(Long playerId){
        Set<TttGame> games = tttGameDao.getAllGamesWithPlayersByPlayerId(playerId);
        List<GameStatisticDto> resultList = new ArrayList<>(games.size());

        for(TttGame game : games){
            String gameResult;
            if(game.getWinner()!=null && game.getWinner().getId().equals(playerId)){
                gameResult = "Победа";
            }else if(game.getWinner() == null && game.getVictoryReasonCode().equals((byte) GameUtils.VICTORY_REASON_DRAW)){
                gameResult = "Ничья";
            }else{
                gameResult = "Поражение";
            }

            String entityName;
            if(game.getPlayer1() != null && game.getPlayer1().getId().equals(playerId)){
                entityName = game.getPlayer2() == null ? "Компьютер" : game.getPlayer2().getLogin();
            }else{
                entityName = game.getPlayer1() == null ? "Компьютер" : game.getPlayer1().getLogin();
            }
            resultList.add(GameStatisticDto.builder()
                            .name(game.getRussianName())
                            .time(game.getStartTime())
                            .code(game.getGameCode())
                            .entityName(entityName)
                            .id(game.getId())
                            .result(gameResult)
                    .build());
        }
        return resultList;
    }
}

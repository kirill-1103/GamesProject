package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.interfaces.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final TttGameService tttGameService;


    public List<Game> getGamesStatisticById(Long playerId){
        List<Game> games = getGames(playerId);
        games.sort((game1, game2) -> -game1.getStartTime().compareTo(game2.getStartTime()));
        return games;
    }

    public List<Game> getPartOfGamesStatisticById(Long playerId, Long from, Long to){
        List<Game> games = getGamesStatisticById(playerId);
        if (from >= games.size()) {
            return Collections.emptyList();
        }
        if (to > games.size()) {
            to = (long) games.size();
        }
        return games.subList(from.intValue(), to.intValue());
    }

    private List<Game> getGames(Long playerId) {
        List<Game> resultList = new ArrayList<>();

        resultList.addAll(tttGameService.getAllGamesByPlayerId(playerId));
//        resultList.addAll(tetrisService.getAllGamesByPlayerId(playerId));
        return resultList;
    }
}

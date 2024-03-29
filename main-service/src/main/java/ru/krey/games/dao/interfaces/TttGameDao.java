package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.games.ttt.TttGame;

import java.util.Optional;
import java.util.Set;

public interface TttGameDao {

    Optional<TttGame> getOneById(Long id);

    Set<TttGame> getAll();

    TttGame saveOrUpdate(TttGame game);

    Set<TttGame> getAllByPlayerId(Long playerId);

    Optional<TttGame> getCurrentGameByPlayerId(Long id);

    Set<TttGame> getAllNoEnded();

    Set<TttGame> getAllGamesWithPlayersByPlayerId(Long playerId);

    Set<TttGame> getAllGamesWithPlayers();

    Optional<Long> getCurrentGameIdByPlayerId(Long playerId);
}

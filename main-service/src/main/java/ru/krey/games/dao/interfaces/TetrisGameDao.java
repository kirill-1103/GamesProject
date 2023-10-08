package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.games.tetris.TetrisGame;

import java.util.Optional;
import java.util.Set;

public interface TetrisGameDao {
    TetrisGame saveOrUpdate(TetrisGame game);

    Optional<TetrisGame> getCurrentGameByPlayerId(Long playerId);

    Optional<TetrisGame> getOneById(Long gameId);

    Set<TetrisGame> getAllByPlayerId(Long playerId);

    Set<TetrisGame> getAllGamesWithPlayersByPlayerId(Long playerId);

}

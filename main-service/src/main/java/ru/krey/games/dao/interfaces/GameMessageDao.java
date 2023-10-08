package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.GameMessage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GameMessageDao {

    Optional<GameMessage> getOneById(Long id);

    GameMessage saveOrUpdate(GameMessage message);

    Set<GameMessage> getAll();

    Set<GameMessage> getAllBySenderId(Long playerId);

    List<GameMessage> getAllByGameIdAndGameCode(Long gameId, Integer gameCode);
}

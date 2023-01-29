package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.GameMessage;

import java.util.Optional;
import java.util.Set;

public interface GameMessageDao {

    Optional<GameMessage> getOneById(Long id);

    GameMessage saveOrUpdate(GameMessage message);

    Set<GameMessage> getAll();

    Set<GameMessage> getAllBySenderId(Long playerId);
}

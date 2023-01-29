package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.TttGame;

import java.util.Optional;
import java.util.Set;

public interface TttGameDao {

    Optional<TttGame> getOneById(Long id);

    Set<TttGame> getAll();

    TttGame saveOrUpdate(TttGame game);

    Set<TttGame> getAllByPlayerId(Long playerId);
}

package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.TttMove;

import java.util.Optional;
import java.util.Set;

public interface TttMoveDao {

    Optional<TttMove> getOneById(Long id);

    Set<TttMove> getAll();

    Set<TttMove> getAllByPlayerId(Long playerId);

    Set<TttMove> getAllByGameId(Long id);

    TttMove saveOrUpdate(TttMove move);


}

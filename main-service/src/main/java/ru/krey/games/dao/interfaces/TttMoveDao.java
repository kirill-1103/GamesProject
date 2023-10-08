package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.games.ttt.TttMove;
import ru.krey.games.dto.TttMoveDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TttMoveDao {

    Optional<TttMove> getOneById(Long id);

    Set<TttMove> getAll();

    Set<TttMove> getAllByPlayerId(Long playerId);

    Set<TttMove> getAllByGameId(Long id);

    TttMove saveOrUpdate(TttMove move);

    List<TttMoveDto> getAllByGameIdOrderedByTime(Long gameId);
}

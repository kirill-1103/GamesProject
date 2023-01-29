package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.TttGame;

import java.util.Optional;

public interface TttGameDao {

    Optional<TttGame> getOneById(Long id);
}

package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PlayerDao {
    Player saveOrUpdate(Player player);

    Optional<Player> getOneById(Long id);

    Set<Player> getAll();

    Optional<Player> getOneByLogin(String login);

    Optional<Player> getOneByEmail(String email);

    List<Player> getAllOrderByRating();

    Long getPlayerTopById(Long id);

    List<Player> getPlayersByPartOfName(String part);

    List<Player> getPlayersByPartOfEmail(String part);

    List<Player> getPlayersWithNameStarts(String part);

    List<Player> getPlayersByLogins(List<String> names);
}

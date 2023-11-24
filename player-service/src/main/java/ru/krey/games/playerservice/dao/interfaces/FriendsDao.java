package ru.krey.games.playerservice.dao.interfaces;

import ru.krey.games.playerservice.domain.Player;

import java.util.List;

public interface FriendsDao  {
    List<Player> getFriends(Long playerId, Long from, Long to);
    void removeFriend(Long playerId, Long friendId);
    boolean friendAlreadyExists(Long playerId, Long friendId);

    void addFriend(Long playerId, Long friendId);
}

package ru.krey.games.playerservice.service.interfaces;

import org.springframework.stereotype.Service;
import ru.krey.games.playerservice.domain.Player;

import java.util.List;

public interface FriendsService {
    List<Player> getFriends(Long playerId, Long from, Long to);
    void removeFriend(Long playerId, Long friendId);
    void addFriend(Long playerId, Long friendId);
}

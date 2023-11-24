package ru.krey.games.playerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.playerservice.dao.interfaces.FriendsDao;
import ru.krey.games.playerservice.domain.Player;
import ru.krey.games.playerservice.error.BadRequestException;
import ru.krey.games.playerservice.service.interfaces.FriendsService;
import ru.krey.games.playerservice.service.interfaces.PlayerService;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class FriendsServiceImpl implements FriendsService {
    private final FriendsDao friendsDao;

    private final PlayerService playerService;

    @Override
    public List<Player> getFriends(Long playerId, Long from, Long to) {
        throwExceptionIfNotExists(playerId);
        if (from > to) {
            throw new BadRequestException("from > to");
        }
        return friendsDao.getFriends(playerId, from, to);
    }

    @Override
    public void removeFriend(Long playerId, Long friendId) {
        throwExceptionIfNotExists(playerId, friendId);
        friendsDao.removeFriend(playerId, friendId);
    }

    @Override
    public void addFriend(Long playerId, Long friendId) {
        if (playerId.equals(friendId)) {
            throw new BadRequestException("player cannot be friends with themselves");
        }
        throwExceptionIfNotExists(playerId, friendId);
        if (friendsDao.friendAlreadyExists(playerId, friendId)) {
            throw new BadRequestException("friend already exists");
        }
        friendsDao.addFriend(playerId, friendId);
    }

    private void throwExceptionIfNotExists(Long... playerIds) {
        Arrays.stream(playerIds).forEach(playerService::getOneById);
    }
}

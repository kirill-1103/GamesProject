package ru.krey.games.playerservice.dao.interfaces;

import ru.krey.games.playerservice.domain.FriendRequest;

import java.util.List;
import java.util.Optional;

public interface FriendRequestDao {
    List<FriendRequest> getReceived(Long playerId);

    List<FriendRequest> getSent(Long playerId);

    void save(FriendRequest request);

    Optional<FriendRequest> getById(Long requestId);

    void update(FriendRequest friendRequest);
}

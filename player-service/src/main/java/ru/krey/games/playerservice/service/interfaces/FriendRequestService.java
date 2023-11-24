package ru.krey.games.playerservice.service.interfaces;

import org.springframework.stereotype.Service;
import ru.krey.games.playerservice.domain.FriendRequest;
import ru.krey.games.playerservice.openapi.model.FriendRequestOpenApi;

import java.util.List;

public interface FriendRequestService {
    void acceptFriendRequest(Long requestId);

    List<FriendRequest> getReceivedFriendRequest(Long playerId);

    List<FriendRequest> getSentFriendRequest(Long playerId);

    void rejectFriendRequest(Long requestId);

    void revokeFriendRequest(Long requestId);

    void sendFriendRequest(FriendRequestOpenApi friendRequestOpenApi);
}

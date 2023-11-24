package ru.krey.games.playerservice.delegate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.playerservice.domain.FriendRequest;
import ru.krey.games.playerservice.domain.Player;
import ru.krey.games.playerservice.openapi.api.FriendsApiDelegate;
import ru.krey.games.playerservice.openapi.model.FriendRequestOpenApi;
import ru.krey.games.playerservice.openapi.model.PlayerOpenApi;
import ru.krey.games.playerservice.service.interfaces.FriendRequestService;
import ru.krey.games.playerservice.service.interfaces.FriendsService;
import ru.krey.games.playerservice.utils.mapper.mapstruct.FriendRequestMapper;
import ru.krey.games.playerservice.utils.mapper.mapstruct.PlayerMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendsDelegateImpl implements FriendsApiDelegate {

    private final FriendsService friendsService;

    private final FriendRequestService friendRequestService;

    private final PlayerMapper playerMapper;

    private final FriendRequestMapper friendRequestMapper;

    @Override
    public ResponseEntity<Void> acceptFriendRequest(Long requestId) {
        friendRequestService.acceptFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<PlayerOpenApi>> getFriends( Long id,
                                                           Long from,
                                                           Long to) {
        List<Player> friends = friendsService.getFriends(id, from, to);
        List<PlayerOpenApi> friendsOpenApi = playerMapper.toOpenApi(friends);
        return ResponseEntity.ok(friendsOpenApi);
    }

    @Override
    public ResponseEntity<List<FriendRequestOpenApi>> getReceivedFriendRequest(Long playerId) {
        List<FriendRequest> friendRequests = friendRequestService.getReceivedFriendRequest(playerId);
        List<FriendRequestOpenApi> friendRequestsOpenApi = friendRequestMapper.toOpenApi(friendRequests);
        return ResponseEntity.ok(friendRequestsOpenApi);
    }

    @Override
    public ResponseEntity<List<FriendRequestOpenApi>> getSentFriendRequest(Long playerId) {
        List<FriendRequest> friendRequests = friendRequestService.getSentFriendRequest(playerId);
        List<FriendRequestOpenApi> friendRequestsOpenApi = friendRequestMapper.toOpenApi(friendRequests);
        return ResponseEntity.ok(friendRequestsOpenApi);
    }

    @Override
    public ResponseEntity<Void> rejectFriendRequest(Long requestId) {
        friendRequestService.rejectFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeFriend(Long friendId,
                                             Long playerId) {
        friendsService.removeFriend(playerId, friendId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> revokeFriendRequest(Long requestId) {
        friendRequestService.revokeFriendRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> sendFriendRequest(FriendRequestOpenApi friendRequestOpenApi) {
        friendRequestService.sendFriendRequest(friendRequestOpenApi);
        return ResponseEntity.ok().build();
    }
}

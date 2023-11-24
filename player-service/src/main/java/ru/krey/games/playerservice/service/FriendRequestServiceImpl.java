package ru.krey.games.playerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.playerservice.dao.interfaces.FriendRequestDao;
import ru.krey.games.playerservice.domain.FriendRequest;
import ru.krey.games.playerservice.error.BadRequestException;
import ru.krey.games.playerservice.openapi.model.FriendRequestOpenApi;
import ru.krey.games.playerservice.openapi.model.FriendResponseEnumOpenApi;
import ru.krey.games.playerservice.service.interfaces.FriendRequestService;
import ru.krey.games.playerservice.service.interfaces.FriendsService;
import ru.krey.games.playerservice.utils.mapper.mapstruct.FriendRequestMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendsService friendsService;

    private final FriendRequestDao friendRequestDao;

    private final FriendRequestMapper friendRequestMapper;

    @Override
    public void acceptFriendRequest(Long requestId) {
        FriendRequest friendRequest = getById(requestId);
        setResponseAndSave(friendRequest, FriendResponseEnumOpenApi.ACCEPT);
        friendsService.addFriend(friendRequest.getSenderId(), friendRequest.getReceiverId());
    }

    @Override
    public List<FriendRequest> getReceivedFriendRequest(Long playerId) {
        return friendRequestDao.getReceived(playerId);
    }

    @Override
    public List<FriendRequest> getSentFriendRequest(Long playerId) {
        return friendRequestDao.getSent(playerId);
    }

    @Override
    public void rejectFriendRequest(Long requestId) {
        FriendRequest friendRequest = getById(requestId);
        setResponseAndSave(friendRequest, FriendResponseEnumOpenApi.REJECT);
    }

    @Override
    public void revokeFriendRequest(Long requestId) {
        FriendRequest friendRequest = getById(requestId);
        setResponseAndSave(friendRequest, FriendResponseEnumOpenApi.REVOKED);
    }

    @Override
    public void sendFriendRequest(FriendRequestOpenApi friendRequestOpenApi) {
        FriendRequest friendRequest = friendRequestMapper.toRequest(friendRequestOpenApi);
        if (friendRequest.getSenderId().equals(friendRequest.getReceiverId())) {
            throw new BadRequestException("player cannot be friends with themselves");
        }
        friendRequest.setResponse(FriendResponseEnumOpenApi.PENDING);
        friendRequest.setRequestDate(LocalDateTime.now());
        friendRequestDao.save(friendRequest);
    }

    private FriendRequest getById(Long requestId) {
        return friendRequestDao.getById(requestId)
                .orElseThrow(() -> new BadRequestException(String.format("Request with id %d not found", requestId)));
    }

    private void setResponseAndSave(FriendRequest friendRequest, FriendResponseEnumOpenApi response) {
        friendRequest.setResponse(FriendResponseEnumOpenApi.REVOKED);
        friendRequest.setResponseDate(LocalDateTime.now());
        friendRequestDao.update(friendRequest);
    }
}

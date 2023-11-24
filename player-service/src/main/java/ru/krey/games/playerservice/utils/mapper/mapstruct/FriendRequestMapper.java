package ru.krey.games.playerservice.utils.mapper.mapstruct;

import org.mapstruct.Mapper;
import ru.krey.games.playerservice.domain.FriendRequest;
import ru.krey.games.playerservice.openapi.model.FriendRequestOpenApi;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {
    FriendRequest toRequest(FriendRequestOpenApi friendRequestOpenApi);
    List<FriendRequest> toRequest(List<FriendRequestOpenApi> friendRequestOpenApi);


    FriendRequestOpenApi toOpenApi(FriendRequest friendRequest);
    List<FriendRequestOpenApi> toOpenApi(List<FriendRequest> friendRequest);
}

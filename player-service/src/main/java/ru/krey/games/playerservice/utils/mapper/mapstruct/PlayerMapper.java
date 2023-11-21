package ru.krey.games.playerservice.utils.mapper.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.krey.games.playerservice.domain.Player;
import ru.krey.games.playerservice.openapi.model.PlayerOpenApi;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    PlayerOpenApi toOpenApi(Player player);

    Player toPlayer(PlayerOpenApi playerOpenApi);

    List<PlayerOpenApi> toOpenApi(List<Player> playerList);

    Set<PlayerOpenApi> toOpenApi(Set<Player> playerSet);
}

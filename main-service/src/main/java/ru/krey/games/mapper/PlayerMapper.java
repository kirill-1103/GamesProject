package ru.krey.games.mapper;

import org.mapstruct.Mapper;
import ru.krey.games.domain.Player;
import ru.krey.games.openapi.model.PlayerOpenApi;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    Player toPlayer(PlayerOpenApi playerOpenApi);
    List<Player> toPlayer(List<PlayerOpenApi> playerOpenApiList);

    PlayerOpenApi toOpenApi(Player player);
}

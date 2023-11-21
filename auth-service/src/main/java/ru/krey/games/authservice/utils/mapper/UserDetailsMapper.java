package ru.krey.games.authservice.utils.mapper;

import org.mapstruct.Mapper;
import ru.krey.games.authservice.domain.UserDetailsImpl;
import ru.krey.games.openapi.model.PlayerOpenApi;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDetailsMapper {
    UserDetailsImpl toUserDetails(PlayerOpenApi playerOpenApi);
}

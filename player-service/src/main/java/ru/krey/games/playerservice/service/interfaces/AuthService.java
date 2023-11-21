package ru.krey.games.playerservice.service.interfaces;

import ru.krey.games.playerservice.domain.Player;

public interface AuthService {
    String createAuthToken(Player player);
}

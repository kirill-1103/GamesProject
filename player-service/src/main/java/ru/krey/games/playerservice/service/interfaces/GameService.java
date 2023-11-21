package ru.krey.games.playerservice.service.interfaces;

public interface GameService {
    Long getCurrentGameId(Long playerId, Integer lastGameCode);
}

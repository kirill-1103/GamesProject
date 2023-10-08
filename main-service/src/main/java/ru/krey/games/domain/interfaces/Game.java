package ru.krey.games.domain.interfaces;

import ru.krey.games.domain.Player;

import java.time.LocalDateTime;
import java.util.Set;

public interface Game {

    Set<Player> getPlayers();

    String getGameName();

    int getGameCode();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    int getDurationInMillis();

    String getRussianName();

    Long getId();

    Player getWinner();

    String getTextGameResultByPlayerId(Long playerId);

    String getEntityNameByPlayerId(Long playerId);
}

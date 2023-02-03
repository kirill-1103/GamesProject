package ru.krey.games.domain;

import lombok.*;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.service.GameService;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class TttGame implements Game {

    private Long id;

    @NonNull
    private Player player1;

    private Player player2;

    @NonNull
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Player winner;

    @NonNull
    private Integer sizeField;

    @NonNull
    private Long player1Time;

    @NonNull
    private Long player2Time;

    @NonNull
    private Long basePlayerTime;

    private Integer actualDuration;

    private Integer complexity;

    @NonNull
    private Byte victoryReasonCode;

    @Override
    public Set<Player> getPlayers() {
        return Set.of(player1,player2);
    }

    @Override
    public String getGameName() {
        return GameService.TttGameName;
    }

    @Override
    public int getGameCode() {
        return GameService.TttGameCode;
    }

    @Override
    public int getDurationInMillis() {
        return actualDuration;
    }
}

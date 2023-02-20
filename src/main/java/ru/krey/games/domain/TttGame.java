package ru.krey.games.domain;

import lombok.*;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.logic.ttt.TttField;
import ru.krey.games.service.GameService;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
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

    //фактическая длительность игры
    private Integer actualDuration;

    private Integer complexity;

    private Byte victoryReasonCode;

    private Byte queue;

    private TttField field;

    @Override
    public Set<Player> getPlayers() {
        return Set.of(player1, player2);
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

    private final int diffTime = 10;//1sec

    private void changePlayer1Time() {
        if (this.player1Time != null && this.basePlayerTime>0) {
            this.player1Time -= diffTime;
        }
    }

    private void changePlayer2Time() {
        if (this.player2Time != null && this.basePlayerTime>0) {
            this.player2Time -= diffTime;
        }
    }

    public void changeGameTime() {
        this.actualDuration += diffTime;
        if (queue == 1) {
            changePlayer1Time();
        } else {
            changePlayer2Time();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TttGame tttGame = (TttGame) o;
        return Objects.equals(id, tttGame.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

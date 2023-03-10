package ru.krey.games.domain;

import lombok.*;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.logic.ttt.TttField;
import ru.krey.games.utils.GameUtils;

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


    public final static String RUSSIAN_NAME = "Крестики нолики";
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
        return GameUtils.TttGameName;
    }

    @Override
    public int getGameCode() {
        return GameUtils.TttGameCode;
    }

    @Override
    public int getDurationInMillis() {
        return actualDuration;
    }

    private final int diffTime = 1;//0.1sec

    private void changePlayer1Time() {
        if (this.player1Time != null && this.basePlayerTime > 0) {
            this.player1Time -= diffTime;
        }
    }

    private void changePlayer2Time() {
        if (this.player2Time != null && this.basePlayerTime > 0) {
            this.player2Time -= diffTime;
        }
    }

    public void changeGameTime() {
        this.actualDuration += diffTime;
        if (queue == 0) {
            changePlayer1Time();
        } else {
            changePlayer2Time();
        }
    }

    //return true if rating has been changed
    public boolean changeRating() {
        if (this.getPlayer2() != null) {
            this.getWinner().plusRating();
            if (this.getWinner().getId().equals(this.getPlayer2().getId())) {
                this.getPlayer1().minusRating();
            } else {
                this.getPlayer2().minusRating();
            }
            return true;
        }
        return false;
    }

    public Player addMoveInFieldReturningMover(Long playerId, Integer x, Integer y) {
        if (this.getPlayer1().getId().equals(playerId)) {
            this.getField().setMove(x, y, TttField.X);
            return this.getPlayer1();
        } else if (this.getPlayer2().getId().equals(playerId)) {
            this.getField().setMove(x, y, TttField.O);
            return this.getPlayer2();
        } else {
            throw new IllegalArgumentException("Wrong player's id");
        }
    }


    public static long getGameTimeFromMinutes(int minutes) {
        final int x = 60 * 10;
        return (long) x * (long) minutes;
    }

    @Override
    public String getRussianName(){
        return RUSSIAN_NAME;
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

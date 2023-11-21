package ru.krey.games.domain.games.ttt;

import lombok.*;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.domain.interfaces.StorableGame;
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
public class TttGame implements Game, StorableGame {


    private final static String RUSSIAN_NAME = "Крестики нолики";
    private Long id;

    private Player player1;

    private Player player2;

    private Long player1Id;

    private Long player2Id;

    private Long winnerId;

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
        return GameUtils.TTT_GAME_NAME;
    }

    @Override
    public int getGameCode() {
        return GameUtils.TTT_GAME_CODE;
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

    public Long getId(){
        return id;
    }

    @Override
    public String getTextGameResultByPlayerId(Long playerId) {
        if(this.winner != null && this.winner.getId().equals(playerId)){
            return "Победа";
        }else if(this.winner == null && this.victoryReasonCode.equals((byte) GameUtils.VICTORY_REASON_DRAW)){
            return "Ничья";
        }
        return "Поражение";
    }

    @Override
    public String getEntityNameByPlayerId(Long playerId) {
        String entityName;
        if(this.player1 != null && this.player1.getId().equals(playerId)){
            entityName = this.player2 == null ? "Компьютер" : this.player2.getLogin();
         }else{
            entityName = this.player1 == null ? "Компьютер" : this.player1.getLogin();
        }
        return entityName;
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

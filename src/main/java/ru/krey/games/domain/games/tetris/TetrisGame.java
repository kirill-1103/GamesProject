package ru.krey.games.domain.games.tetris;

import lombok.*;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.logic.tetris.TetrisField;
import ru.krey.games.logic.tetris.TetrisFigureUtils;
import ru.krey.games.utils.GameUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@ToString
public class TetrisGame implements Game {

    private final static String RUSSIAN_NAME = "Тетрис";

    private Long id;

    @NonNull
    private Player player1;

    private Player player2;

    @NonNull
    private LocalDateTime startTime;

    @NonNull
    private Long player1Time;

    private Long player2Time;

    @NonNull
    private Integer player1Points;

    @NonNull Integer player2Points;

    private LocalDateTime endTime;

    private Player winner;

    private Integer duration;

    private int[][] field1;

    private int[][] field2;


    @Override
    public Set<Player> getPlayers() {
        return Set.of(player1, player2);
    }

    @Override
    public String getGameName() {
        return GameUtils.TETRIS_GAME_NAME;
    }

    @Override
    public int getGameCode() {
        return GameUtils.TETRIS_GAME_CODE;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public int getDurationInMillis() {
        return duration;
    }

    @Override
    public String getRussianName() {
        return RUSSIAN_NAME;
    }


    @Override
    public String getTextGameResultByPlayerId(Long playerId) {
        if(this.winner != null && this.winner.getId().equals(playerId)){
            return "Победа";
        }else if(this.winner != null){
            return "Поражение";
        }
        return "undefined";
    }

    @Override
    public String getEntityNameByPlayerId(Long playerId) {
        String entityName;
        if(player1 != null && player1.getId().equals(playerId)){
            entityName = player2 == null ? "---" : player2.getLogin();
        }else{
            entityName = player1 == null ? "---" : player1.getLogin();
        }
        return entityName;
    }

    public boolean changeRating() {
        if (this.getPlayer2() != null && this.getWinner() != null) {
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

}

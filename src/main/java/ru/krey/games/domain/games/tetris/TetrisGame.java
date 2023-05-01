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

}

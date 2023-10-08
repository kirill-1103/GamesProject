package ru.krey.games.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.krey.games.domain.Player;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TetrisDto {
    private List<List<Integer>> field1;
    private List<List<Integer>> field2;
    private Integer player1Points;
    private Integer player2Points;
    private Integer player1Time;
    private Integer player2Time;
    private List<Integer> lastRemovedRows1;
    private List<Integer> lastRemovedRows2;
    private Player player1;
    private Player player2;
    private int[][] nextFigure1;
    private int[][] nextFigure2;
    private Player winner;
    private boolean game1Stop;
    private  boolean game2Stop;
    private Long gameId;
}

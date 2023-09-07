package ru.krey.games.domain.games.tetris;

import lombok.*;
import ru.krey.games.domain.Player;
import ru.krey.games.logic.tetris.TetrisLogic;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TetrisGameInfo {
    private  Player player1;
    private  Player player2;
    private  TetrisLogic tetris1;
    private  TetrisLogic tetris2;
    private  Long gameId;
    private  Player winner;
}

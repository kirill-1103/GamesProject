package ru.krey.games.domain.games.tetris;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.krey.games.domain.Player;
import ru.krey.games.logic.tetris.TetrisLogic;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class TetrisGameInfo {
    private final Player player1;
    private final Player player2;
    private final TetrisLogic tetris1;
    private final TetrisLogic tetris2;
    private final Long gameId;
    private final Player winner;
}

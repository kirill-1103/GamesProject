package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;
import ru.krey.games.domain.TttMove;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TttMoveService {
    private final TttMoveDao moveDao;

    public void saveMove(TttGame game, Player mover, Integer x, Integer y) {
        TttMove move = TttMove.builder()
                .absoluteTime(LocalDateTime.now())
                .game(game)
//                .gameTimeMillis((int) ((game.getBasePlayerTime() * 2 - (game.getPlayer1Time() + game.getPlayer2Time())) * 100))
                .gameTimeMillis(game.getActualDuration())
                .xCoordinate(x)
                .yCoordinate(y)
                .player(mover)
                .build();

        moveDao.saveOrUpdate(move);
    }
}

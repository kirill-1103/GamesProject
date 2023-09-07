package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TetrisGameDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.games.tetris.TetrisGame;
import ru.krey.games.domain.games.tetris.TetrisGameInfo;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.logic.tetris.TetrisLogic;
import ru.krey.games.utils.GameUtils;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TetrisService {
    private final TetrisGameDao tetrisDao;

    private final PlayerDao playerDao;

    public TetrisGame newGame(Long player1Id, Long player2Id) {
        if (player1Id == null) {
            throw new BadRequestException("Не удалось создать игру Tetris.");
        }

        if (player2Id != null) {
            Random random = new Random();
            long randomInt = random.nextInt(2);
            if (randomInt == 1) {
                long temp = player1Id;
                player1Id = player2Id;
                player2Id = temp;
            }
        }

        Player player1 = playerDao.getOneById(player1Id).orElseThrow(NotFoundException::new);
        Player player2 = player2Id == null ? null : playerDao.getOneById(player2Id).orElseThrow(NotFoundException::new);

        TetrisGame tetrisGame = TetrisGame.builder()
                .duration(0)
                .player1Points(0)
                .player2Points(0)
                .player1(player1)
                .player2(player2)
                .startTime(LocalDateTime.now())
                .player1Time(0L)
                .player2Time(0L)
                .build();
        TetrisGame savedGame = tetrisDao.saveOrUpdate(tetrisGame);
        player1.setLastGameCode(GameUtils.TETRIS_GAME_CODE);
        playerDao.saveOrUpdate(player1);
        if (player2 != null) {
            player2.setLastGameCode(GameUtils.TETRIS_GAME_CODE);
            playerDao.saveOrUpdate(player2);
        }
        return savedGame;
    }

    public void saveGame(TetrisGameInfo gameInfo) {
        TetrisGame game = tetrisDao.getOneById(gameInfo.getGameId())
                .orElseThrow(() -> new NotFoundException("Не удалось найти игру Тетрис с id:" + gameInfo.getGameId()));
        game.setDuration(Math.abs(gameInfo.getTetris1().getTimeInMillis()));
        game.setEndTime(LocalDateTime.now());
        game.setPlayer1(gameInfo.getPlayer1());
        game.setPlayer1Points(gameInfo.getTetris1().getPoints());
        game.setPlayer1Time((long) gameInfo.getTetris1().getTimeInMillis());
        game.setField1(gameInfo.getTetris1().getField().getFieldArray());

        if(gameInfo.getWinner() != null){
            game.setWinner(gameInfo.getWinner());
        }

        if (gameInfo.getPlayer2() != null) {
            game.setPlayer2(gameInfo.getPlayer2());
            game.setField2(gameInfo.getTetris2().getField().getFieldArray());
            if (game.getWinner() == null) {
                game.setWinner(
                        gameInfo.getTetris1().getPoints() > gameInfo.getTetris2().getPoints() ?
                                gameInfo.getPlayer1() :
                                (gameInfo.getTetris1().getPoints() == gameInfo.getTetris2().getPoints() ?
                                        null : gameInfo.getPlayer2())
                );
            }
            game.setPlayer2Points(gameInfo.getTetris2().getPoints());
            game.setPlayer2Time((long) gameInfo.getTetris2().getTimeInMillis());
        } else {
            if(game.getWinner() != null){
                game.setWinner(gameInfo.getPlayer1());
            }
        }
        if (game.changeRating()) {
            playerDao.saveOrUpdate(game.getPlayer1());
            playerDao.saveOrUpdate(game.getPlayer2());
        }
        tetrisDao.saveOrUpdate(game);
    }

}

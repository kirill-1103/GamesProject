package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.controller.TttMoveController;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.games.ttt.TttGame;
import ru.krey.games.domain.games.ttt.TttMove;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.dto.TttMoveDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.logic.ttt.TttField;
import ru.krey.games.utils.GameUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TttGameService {
    private final TttGameDao gameDao;
    private final PlayerDao playerDao;

    private final TttMoveDao moveDao;

    public TttGame newGame(Long player1Id, Long player2Id,
                               Integer fieldSize, Long minutes,
                               Integer complexity) {
        if (player1Id == null || fieldSize == null || minutes == null) {
            throw new BadRequestException("Не удалось создать игру - неверные входные данные");
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

        Long time = TttGame.getGameTimeFromMinutes(minutes.intValue());

        TttGame newGame = TttGame.builder()
                .actualDuration(0)
                .basePlayerTime(time)
                .player1Time(time + 30)
                .player2Time(time)
                .player1(player1)
                .player2(player2)
                .startTime(LocalDateTime.now())
                .complexity(complexity)
                .sizeField(fieldSize)
                .queue((byte) 0)
                .build();
        if (minutes == -1) {
            newGame.setBasePlayerTime(minutes);
        }
        TttGame savedGame = gameDao.saveOrUpdate(newGame);
        player1.setLastGameCode(GameUtils.TTT_GAME_CODE);
        playerDao.saveOrUpdate(player1);
        if (player2 != null) {
            player2.setLastGameCode(GameUtils.TTT_GAME_CODE);
            playerDao.saveOrUpdate(player2);
        }
        savedGame.setField(new TttField(fieldSize));
        return savedGame;
    }

    public Optional<TttGame> getOneById(Long id){
        return gameDao.getOneById(id);
    }

    public void setEndedGameInDb(TttGame game){
        game.setEndTime(LocalDateTime.now());
        gameDao.saveOrUpdate(game);
        if(game.changeRating()){
            playerDao.saveOrUpdate(game.getPlayer1());
            playerDao.saveOrUpdate(game.getPlayer2());
        }
    }

    public Optional<TttGame> getCurrentGameByPlayerId(Long id){
        return gameDao.getCurrentGameByPlayerId(id);
    }

    public Set<TttGame> getAllGamesByPlayerId(Long id){
        return gameDao.getAllGamesWithPlayersByPlayerId(id);
    }

    public List<TttMoveDto> getGameMoves(Long gameId){
        TttGame game = gameDao.getOneById(gameId).orElseThrow(NotFoundException::new);
        return getGameMoves(game);
    }

    public List<TttMoveDto> getGameMoves(TttGame game){
        return moveDao.getAllByGameIdOrderedByTime(game.getId());
    }
}

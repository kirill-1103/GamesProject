package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.krey.games.controller.TttGameController;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.logic.ttt.TttField;
import ru.krey.games.utils.GameUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class TttGameService {
    private final TttGameDao gameDao;

    private final TttMoveDao moveDao;

    private final static Logger log = LoggerFactory.getLogger(TttGameService.class);

    private final PlayerDao playerDao;

    private final ConversionService conversionService;

    private final SimpMessagingTemplate messagingTemplate;

    private final ExecutorService executorForSave = Executors.newSingleThreadExecutor();

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
        player1.setLastGameCode(GameUtils.TttGameCode);
        playerDao.saveOrUpdate(player1);
        if (player2 != null) {
            player2.setLastGameCode(GameUtils.TttGameCode);
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

}

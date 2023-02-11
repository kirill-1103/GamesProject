package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.AuthService;
import ru.krey.games.service.GameService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api/ttt_game")
@RequiredArgsConstructor
public class TttGameController {
//    @MessageMapping("/time")

    private final TttGameDao gameDao;

    private final static Logger log = LoggerFactory.getLogger(TttGameController.class);

    private final PlayerDao playerDao;

    private final ConversionService conversionService;

    private final SimpMessagingTemplate messagingTemplate;

    private final AuthService authService;

    private Set<TttGame> savedGames = new HashSet<>();


    @PostMapping("/new")
    public @ResponseBody TttGameDto createGame(
            @RequestParam("player1_id") Long player1Id, @RequestParam(value = "player2_id", required = false) Long player2Id,
            @RequestParam("field_size") Integer fieldSize, @RequestParam("base_player_time") Long minutes,
            @RequestParam(value = "complexity", required = false) Integer complexity
    ) {
        if (player1Id == null || fieldSize == null || minutes == null) {
            throw new BadRequestException("Не удалось создать игру - неверные входные данные");
        }

        Player player1 = playerDao.getOneById(player1Id).orElseThrow(NotFoundException::new);
        Player player2 = player2Id == null ? null : playerDao.getOneById(player2Id).orElseThrow(NotFoundException::new);


        final Integer x = 60 * 10;//счет по 1/10 секунде

        TttGame newGame = TttGame.builder()
                .actualDuration(0)
                .basePlayerTime(minutes * x)
                .player1Time(minutes * x)
                .player2Time(minutes * x)
                .player1(player1)
                .player2(player2)
                .startTime(LocalDateTime.now())
                .complexity(complexity)
                .sizeField(fieldSize)
                .queue((byte) 1)
                .build();
        TttGame savedGame = gameDao.saveOrUpdate(newGame);
        player1.setLastGameCode(GameService.TttGameCode);
        playerDao.saveOrUpdate(player1);
        savedGames.add(savedGame);
        return conversionService.convert(savedGame, TttGameDto.class);
    }

    @GetMapping("/{id}")
    public TttGameDto getOneById(@PathVariable("id") Long id) {
        return conversionService.convert(savedGames.stream().filter(savedGame -> savedGame.getId().equals(id))
                .findAny()
                .orElse(gameDao.getOneById(id)
                        .orElseThrow(() -> new NotFoundException("Игра не найдена"))), TttGameDto.class);
    }

    @MessageMapping("/connect")
    @Scheduled(fixedRate = 1000)
//    @SendTo("/topic/ttt_game")
    public void topicTttGame() {
        if (savedGames.isEmpty()) {
            Set<TttGame> gamesNoEnded = gameDao.getAllNoEnded();
            savedGames = new HashSet<>(gamesNoEnded);
        }
        savedGames.forEach((game) -> {
            changeGameTime(game);
//                TttGame gameFromDb = gameDao.saveOrUpdate(game);//сохраним, когда игра закончитсяы
//                if(game.getEndTime()!=null){
//                    gameDao.saveOrUpdate(game);
//                    return;
//                }
            messagingTemplate.convertAndSend("/topic/ttt_game/" + game.getId()
                    , conversionService.convert(game, TttGameDto.class));
        });
    }

    @PostMapping("/change_queue")
    public void changeQueue(@RequestParam("game_id") Long gameId) {
        TttGame game = savedGames
                .stream()
                .filter(savedGame -> savedGame.getId().equals(gameId))
                .findAny()
                .orElseThrow(RuntimeException::new);

        game.setQueue((byte) (1 - game.getQueue()));
//        gameDao.saveOrUpdate(game);
    }

    private void changeGameTime(TttGame game) {
        if (game.getQueue() == 1) {
            game.changePlayer1Time();
        } else {
            game.changePlayer2Time();
        }
    }

}

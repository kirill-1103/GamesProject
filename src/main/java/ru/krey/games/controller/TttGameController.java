package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;
import ru.krey.games.domain.TttMove;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.logic.ttt.TttField;
import ru.krey.games.service.AuthService;
import ru.krey.games.service.GameService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("api/ttt_game")
@RequiredArgsConstructor
public class TttGameController {

    private final TttGameDao gameDao;

    private final TttMoveDao moveDao;

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
        if(minutes == -1){
            newGame.setBasePlayerTime(minutes);
        }
        TttGame savedGame = gameDao.saveOrUpdate(newGame);
        player1.setLastGameCode(GameService.TttGameCode);
        playerDao.saveOrUpdate(player1);
        if (player2 != null) {
            player2.setLastGameCode(GameService.TttGameCode);
        }
        savedGame.setField(new TttField(fieldSize));
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
    public void topicTttGame() {
        if (savedGames.isEmpty()) {
            Set<TttGame> gamesNoEnded = gameDao.getAllNoEnded();
            savedGames = new HashSet<>(gamesNoEnded);
        }
        savedGames.forEach((game) -> {
            if(Objects.isNull(game.getVictoryReasonCode())){
               return;
            }
            game.changeGameTime();
            if (game.getField() == null) {
                game.setField(new TttField(game.getSizeField()));
            }
            if (game.getPlayer1Time() == 0 || game.getPlayer2Time() == 0) {
                endGameByTime(game);
            }
            messagingTemplate.convertAndSend("/topic/ttt_game/" + game.getId()
                    , conversionService.convert(game, TttGameDto.class));
        });
    }

    @PostMapping("/surrender")
    public void surrender(@RequestParam("game_id") Long gameId,
                          @RequestParam("player_id") Long playerId) {
        TttGame game = getGameFromSaved(gameId);
        if (game.getPlayer1().getId().equals(playerId)) {
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_PLAYER1_LOSE);
            game.setWinner(game.getPlayer2());
        } else if (game.getPlayer2().getId().equals(playerId)) {
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_PLAYER2_LOSE);
            game.setWinner(game.getPlayer1());
        } else {
            throw new BadRequestException();
        }
        saveGameInDb(game);
    }

    @PostMapping("/make_move")
    public void makeMove(@RequestParam("game_id") Long gameId,
                         @RequestParam("player_id") Long playerId,
                         @RequestParam("x") Integer x,
                         @RequestParam("y") Integer y) {
        TttGame game = getGameFromSaved(gameId);

        Player mover = addMoveInFieldReturningMover(game, playerId, x, y);

        game.setQueue((byte) (1 - game.getQueue()));

        int winner = game.getField().getWinner();
        if (winner != TttField.NONE) {
            endGameByWinner(game, winner);
        }

        int[] coords = null;

        if (game.getPlayer2() == null && winner == TttField.NONE) {
            coords = makeComputerMoveReturningCoords(game);
        }

        messagingTemplate.convertAndSend("/topic/ttt_game/" + gameId
                , conversionService.convert(game, TttGameDto.class));

        saveMove(game, mover, x, y);

        if (!Objects.isNull(coords)) {
            saveMove(game, null, coords[0], coords[1]);
        }
    }

    private int[] makeComputerMoveReturningCoords(TttGame game) {
        int[] coords = game.getField().randomMove();
        game.setQueue((byte) (1 - game.getQueue()));

        int winner = game.getField().getWinner();
        if (winner != TttField.NONE) {
            endGameByWinner(game, winner);
        }

        return coords;
    }

    private void saveMove(TttGame game, Player mover, Integer x, Integer y) {
        TttMove move = TttMove.builder()
                .absoluteTime(LocalDateTime.now())
                .game(game)
                .gameTimeMillis((int) ((game.getBasePlayerTime() * 2 - (game.getPlayer1Time() + game.getPlayer2Time())) * 100))
                .xCoordinate(x)
                .yCoordinate(y)
                .player(mover)
                .build();

        moveDao.saveOrUpdate(move);
    }

    private void endGameByWinner(TttGame game, int winner) {
        setWinner(game, winner);
        saveGameInDb(game);
    }

    private void endGameByTime(TttGame game) {
        if (game.getPlayer1Time() == 0) {
            game.setWinner(game.getPlayer1());
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_PLAYER1_TIME_WIN);
        } else {
            game.setWinner(game.getPlayer2());
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_PLAYER2_TIME_WIN);
        }
        saveGameInDb(game);
    }

    private void setWinner(TttGame game, int winner) {
        if (winner == TttField.X) {
            game.setWinner(game.getPlayer1());
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_PLAYER1_WIN);
        } else if (winner == TttField.O) {
            if (game.getPlayer2() != null) {
                game.setWinner(game.getPlayer2());
            }
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_PLAYER2_WIN);
        } else if (winner == TttField.DRAW) {
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_DRAW);
        } else {
            throw new RuntimeException();
        }
    }

    private TttGame getGameFromSaved(Long id) {
        return savedGames
                .stream()
                .filter(savedGame -> savedGame.getId().equals(id))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private Player addMoveInFieldReturningMover(TttGame game, Long playerId,
                                                Integer x, Integer y) {
        if (game.getPlayer1().getId().equals(playerId)) {
            game.getField().setMove(x, y, TttField.X);
            return game.getPlayer1();
        } else if (game.getPlayer2().getId().equals(playerId)) {
            game.getField().setMove(x, y, TttField.O);
            return game.getPlayer2();
        } else {
            throw new BadRequestException();
        }
    }

    private void saveGameInDb(TttGame game) {
        new Thread(()->{
            game.setEndTime(LocalDateTime.now());
//            game.setActualDuration((int) (game.getBasePlayerTime()*2-game.getPlayer1Time()-game.getPlayer2Time()));
            gameDao.saveOrUpdate(game);
            savedGames.removeIf((g) -> g.getId().equals(game.getId()));
            messagingTemplate.convertAndSend("/topic/ttt_game/" + game.getId()
                    , conversionService.convert(game, TttGameDto.class));
            log.info("Game "+game.getId()+" saved");
        }).start();
    }
}

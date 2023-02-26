package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;
import ru.krey.games.domain.TttMove;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.dto.TttSearchDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.logic.ttt.TttField;
import ru.krey.games.service.AuthService;
import ru.krey.games.service.GameService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final ExecutorService executorForSave = Executors.newSingleThreadExecutor();

    private Set<TttGame> savedGames = new ConcurrentSkipListSet<>(Comparator.comparingLong(TttGame::getId));

    private Deque<TttSearchDto> searches = new ArrayDeque<>();

    @PostMapping("/new")
    public @ResponseBody TttGameDto newGame(
            @RequestParam("player1_id") Long player1Id, @RequestParam(value = "player2_id", required = false) Long player2Id,
            @RequestParam("field_size") Integer fieldSize, @RequestParam("base_player_time") Long minutes,
            @RequestParam(value = "complexity", required = false) Integer complexity
    ) {
        TttGame savedGame = createGame(player1Id, player2Id, fieldSize, minutes, complexity);
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

    //    @MessageMapping("/connect")
    @Scheduled(fixedRate = 100)
    private void gameProcessing() {
//        if (savedGames.isEmpty()) {
//            Set<TttGame> gamesNoEnded = gameDao.getAllNoEnded();
//            savedGames = new ConcurrentSkipListSet<>(Comparator.comparingLong(TttGame::getId));
//            savedGames.addAll(gamesNoEnded);
//        }
        savedGames.forEach((game) -> {
            if(Objects.isNull(game.getEndTime())){
                if (Objects.isNull(game.getVictoryReasonCode())) {
                    return;
                }
                game.changeGameTime();
                if (game.getField() == null) {
                    game.setField(new TttField(game.getSizeField()));
                }
                if (game.getPlayer1Time() == 0 || game.getPlayer2Time() == 0) {
                    game.setEndTime(LocalDateTime.now());
                    endGameByTime(game);
                }
                messagingTemplate.convertAndSend("/topic/ttt_game/" + game.getId()
                        , conversionService.convert(game, TttGameDto.class));
            }
        });
    }

    @PostMapping("/surrender")
    public void surrender(@RequestParam("game_id") Long gameId,
                          @RequestParam("player_id") Long playerId) {
        TttGame game = getGameFromSaved(gameId);

        savedGames.removeIf((g) -> game.getId().equals(g.getId()));

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

    @PostMapping("/search")
    public void startSearch(@RequestBody TttSearchDto search) {
        searches.stream()
                .filter(s -> s.getPlayerId().equals(search.getPlayerId()))
                .findAny()
                .ifPresent(
                        (player) -> {
                            throw new RuntimeException("Игрок уже есть в поиске");
                        }
                );

        TttSearchDto otherSearch = searchIsReady(search);

        if (otherSearch == null) {
            searches.add(search);
        } else {
            TttGame savedGame = createGame(search.getPlayerId(), otherSearch.getPlayerId(),
                    otherSearch.getSizeField(), otherSearch.getBasePlayerTime(), null);
            savedGames.add(savedGame);
            messagingTemplate.convertAndSend("/topic/ttt_player_search_ready/" + savedGame.getPlayer1().getId()
                    , conversionService.convert(savedGame, TttGameDto.class));
            messagingTemplate.convertAndSend("/topic/ttt_player_search_ready/" + savedGame.getPlayer2().getId()
                    , conversionService.convert(savedGame, TttGameDto.class));
        }
    }

    @PostMapping("/stop_search")
    public void stopSearch(@RequestBody TttSearchDto searchDto) {
        if (Objects.isNull(searchDto) || Objects.isNull(searchDto.getPlayerId())) {
            throw new BadRequestException("Player identifier is null");
        }
        searches.removeIf(search -> search.getPlayerId().equals(searchDto.getPlayerId()));
        log.info("Player with id " + searchDto.getPlayerId() + " has stopped searching");
    }


    private TttSearchDto searchIsReady(TttSearchDto search) {
        Iterator<TttSearchDto> iterator = searches.iterator();
        while (iterator.hasNext()) {
            TttSearchDto s = iterator.next();
            if ((Objects.isNull(search.getBasePlayerTime()) ||
                    Objects.isNull(s.getBasePlayerTime()) ||
                    s.getBasePlayerTime().equals(search.getBasePlayerTime())) &&
                    (Objects.isNull(search.getSizeField())
                            || Objects.isNull(s.getSizeField())
                            || s.getSizeField().equals(search.getSizeField()))) {
                Long basePlayerTime = search.getBasePlayerTime() == null ? s.getBasePlayerTime() : search.getBasePlayerTime();
                Integer sizeField = search.getSizeField() == null ? s.getSizeField() : search.getSizeField();

                basePlayerTime = basePlayerTime == null ? -1 : basePlayerTime;

                List<Integer> list = new ArrayList<>(List.of(3, 5, 7, 13));
                Collections.shuffle(list);
                sizeField = sizeField == null ? list.get(0) : sizeField;
                s.setSizeField(sizeField);
                s.setBasePlayerTime(basePlayerTime);
                iterator.remove();
                return s;
            }
        }
        return null;
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
            game.setWinner(game.getPlayer2());
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_PLAYER2_TIME_WIN);
        } else {
            game.setWinner(game.getPlayer1());
            game.setVictoryReasonCode((byte) GameService.VICTORY_REASON_PLAYER1_TIME_WIN);
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
                .orElseThrow(BadRequestException::new);
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
        executorForSave.execute(() -> {
            game.setEndTime(LocalDateTime.now());
//            game.setActualDuration((int) (game.getBasePlayerTime()*2-game.getPlayer1Time()-game.getPlayer2Time()));
            gameDao.saveOrUpdate(game);
            changeRating(game);
            savedGames.removeIf((g) -> g.getId().equals(game.getId()));
            messagingTemplate.convertAndSend("/topic/ttt_game/" + game.getId()
                    , conversionService.convert(game, TttGameDto.class));
            log.info("Game " + game.getId() + " saved");
        });
    }

    private void changeRating(TttGame game) {
        if (game.getPlayer2() != null) {
            game.getWinner().plusRating();
            if (game.getWinner().getId().equals(game.getPlayer2().getId())) {
                game.getPlayer1().minusRating();
            } else {
                game.getPlayer2().minusRating();
            }
            playerDao.saveOrUpdate(game.getPlayer1());
            playerDao.saveOrUpdate(game.getPlayer2());
        }
    }

    private TttGame createGame(Long player1Id, Long player2Id,
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
        player1.setLastGameCode(GameService.TttGameCode);
        playerDao.saveOrUpdate(player1);
        if (player2 != null) {
            player2.setLastGameCode(GameService.TttGameCode);
        }
        savedGame.setField(new TttField(fieldSize));
        return savedGame;
    }
}

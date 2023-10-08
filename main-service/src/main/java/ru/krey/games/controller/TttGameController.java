package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.games.ttt.TttGame;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.dto.TttSearchDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.logic.ttt.TttField;
import ru.krey.games.service.TttGameService;
import ru.krey.games.service.TttMoveService;
import ru.krey.games.state.GameKeeper;
import ru.krey.games.utils.GameUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("api/ttt_game")
@RequiredArgsConstructor
public class TttGameController {
    private final TttGameService gameService;

    private final TttMoveService moveService;

    private final static Logger log = LoggerFactory.getLogger(TttGameController.class);

    private final ConversionService conversionService;

    private final SimpMessagingTemplate messagingTemplate;

    private final ExecutorService executorForSave = Executors.newSingleThreadExecutor();

    private final Deque<TttSearchDto> searches = new ArrayDeque<>();

    private final GameKeeper gameKeeper;

    @PostMapping("/new")
    public @ResponseBody TttGameDto newGame(
            @RequestParam("player1_id") Long player1Id, @RequestParam(value = "player2_id", required = false) Long player2Id,
            @RequestParam("field_size") Integer fieldSize, @RequestParam("base_player_time") Long minutes,
            @RequestParam(value = "complexity", required = false) Integer complexity
    ) {

        TttGame savedGame = gameService.newGame(player1Id, player2Id, fieldSize, minutes, complexity);
        gameKeeper.addGame(savedGame);
        return conversionService.convert(savedGame, TttGameDto.class);
    }

    @GetMapping("/{id}")
    public TttGameDto getOneById(@PathVariable("id") Long id) {
        return conversionService.convert(gameKeeper.getById(id)
                .orElse(gameService.getOneById(id)
                        .orElseThrow(() -> new NotFoundException("Игра не найдена"))),
                TttGameDto.class);
    }

    //TODO: все schedule вынести в отдельные классы
    @Scheduled(fixedRate = 100)
    private void gameProcessing() {
        gameKeeper.forEach((g) -> {
            TttGame game = (TttGame) g;
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
        TttGame game = (TttGame) gameKeeper.getById(gameId).orElseThrow(BadRequestException::new);

        gameKeeper.removeById(gameId);

        if (game.getPlayer1().getId().equals(playerId)) {
            game.setVictoryReasonCode((byte) GameUtils.VICTORY_REASON_PLAYER1_LOSE);
            game.setWinner(game.getPlayer2());
        } else if (game.getPlayer2().getId().equals(playerId)) {
            game.setVictoryReasonCode((byte) GameUtils.VICTORY_REASON_PLAYER2_LOSE);
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
        TttGame game = (TttGame) gameKeeper.getById(gameId).orElseThrow(BadRequestException::new);

        Player mover;
        try{
            mover = game.addMoveInFieldReturningMover(playerId, x, y);
        }catch(IllegalArgumentException e){
            throw new BadRequestException(e);
        }

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

        moveService.saveMove(game,mover,x,y);

        if (!Objects.isNull(coords)) {
            moveService.saveMove(game, null, coords[0], coords[1]);
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

        TttSearchDto otherSearch = getReadySearch(search);

        if (otherSearch == null) {
            searches.add(search);
        } else {
            TttGame savedGame = gameService.newGame(search.getPlayerId(), otherSearch.getPlayerId(),
                    otherSearch.getSizeField(), otherSearch.getBasePlayerTime(), null);
            gameKeeper.addGame(savedGame);
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


    private TttSearchDto getReadySearch(TttSearchDto search) {
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

    private void endGameByWinner(TttGame game, int winner) {
        setWinner(game, winner);
        saveGameInDb(game);
    }

    private void endGameByTime(TttGame game) {
        if (game.getPlayer1Time() == 0) {
            game.setWinner(game.getPlayer2());
            game.setVictoryReasonCode((byte) GameUtils.VICTORY_REASON_PLAYER2_TIME_WIN);
        } else {
            game.setWinner(game.getPlayer1());
            game.setVictoryReasonCode((byte) GameUtils.VICTORY_REASON_PLAYER1_TIME_WIN);
        }
        saveGameInDb(game);
    }

    private void setWinner(TttGame game, int winner) {
        if (winner == TttField.X) {
            game.setWinner(game.getPlayer1());
            game.setVictoryReasonCode((byte) GameUtils.VICTORY_REASON_PLAYER1_WIN);
        } else if (winner == TttField.O) {
            if (game.getPlayer2() != null) {
                game.setWinner(game.getPlayer2());
            }
            game.setVictoryReasonCode((byte) GameUtils.VICTORY_REASON_PLAYER2_WIN);
        } else if (winner == TttField.DRAW) {
            game.setVictoryReasonCode((byte) GameUtils.VICTORY_REASON_DRAW);
        } else {
            throw new RuntimeException();
        }
    }

    private void saveGameInDb(TttGame game) {
        executorForSave.execute(() -> {
            gameService.setEndedGameInDb(game);
            gameKeeper.removeById(game.getId());
            messagingTemplate.convertAndSend("/topic/ttt_game/" + game.getId()
                    , conversionService.convert(game, TttGameDto.class));
            log.info("Game " + game.getId() + " saved");
        });
    }

}

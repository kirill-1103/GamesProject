package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.domain.games.tetris.TetrisGame;
import ru.krey.games.domain.games.tetris.TetrisGameInfo;
import ru.krey.games.domain.interfaces.StorableGame;
import ru.krey.games.dto.TetrisDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.logic.tetris.TetrisField;
import ru.krey.games.logic.tetris.TetrisFigureUtils;
import ru.krey.games.logic.tetris.TetrisLogic;
import ru.krey.games.service.TetrisService;
import ru.krey.games.state.GameKeeper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

/*
    TODO: в игре с человеком время игры и следующие фигуры устанавливаются из контроллера
    в классе  figureutils реализована генерация фигур. за ними надо следить и дополнять при необходимости
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("api/tetris_game")
public class TetrisGameController {
    private final TetrisService tetrisService;

    private final GameKeeper gameKeeper;

    private final Deque<Long> searches = new ArrayDeque<>();

    private final int countNextFigures = 20;

    private final int loopTime = 25;

    private LocalDateTime currentIterationTime;

    private final SimpMessagingTemplate messagingTemplate;

    private final static Logger log = LoggerFactory.getLogger(TttGameController.class);



    @PostMapping("/computer")
    public TetrisDto computerGame(
            @RequestParam("player_id") Long playerId,
            @RequestParam(value = "field_width", required = false) Integer fieldWidth,
            @RequestParam(value = "field_height", required = false) Integer fieldHeight
    ) {
        TetrisGame newGame = tetrisService.newGame(playerId, null);
        TetrisLogic logic1 = new TetrisLogic(new TetrisField(TetrisFigureUtils.generateFigures(countNextFigures)));

        TetrisGameInfo gameInfo = TetrisGameInfo.builder()
                .gameId(newGame.getId())
                .tetris1(logic1)
                .player1(newGame.getPlayer1())
                .build();
        gameKeeper.addGame(gameInfo);

        return TetrisDto.builder()
                .game2Stop(false)
                .game1Stop(false)
                .field1(gameInfo.getTetris1().getField().getField())
                .gameId(newGame.getId())
                .build();
    }

    @PostMapping("/search")
    public void startSearch(@RequestParam("player_id") Long playerId) {
        searches.stream()
                .filter(s -> s.equals(playerId))
                .findAny()
                .ifPresent(
                        (player) -> {
                            throw new RuntimeException("Игрок уже есть в поиске");
                        }
                );

        if(!searches.isEmpty()){
            Long id = searches.poll();
            TetrisGame newGame = tetrisService.newGame(id,playerId);
            TetrisLogic logic1 = new TetrisLogic(new TetrisField(TetrisFigureUtils.generateFigures(countNextFigures)));
            TetrisLogic logic2 = new TetrisLogic(new TetrisField(logic1.getField().getNextFigures()));
            TetrisGameInfo gameInfo = TetrisGameInfo.builder()
                    .gameId(newGame.getId())
                    .tetris1(logic1)
                    .tetris2(logic2)
                    .player1(newGame.getPlayer1())
                    .player2(newGame.getPlayer2())
                    .build();
            gameKeeper.addGame(gameInfo);
            messagingTemplate.convertAndSend("/topic/tetris_player_search_ready/" + newGame.getPlayer1().getId()
                    , fromGameInfoToDto(gameInfo));
            messagingTemplate.convertAndSend("/topic/tetris_player_search_ready/" + newGame.getPlayer2().getId()
                    , fromGameInfoToDto(gameInfo));
        }else{
            searches.add(playerId);
        }
    }

    @PostMapping("/stop_search")
    public void stopSearch(@RequestParam("player_id") Long playerId){
        if(playerId == null){
            throw new BadRequestException("Player identifier is null");
        }
        searches.removeIf(search -> search.equals(playerId));
        log.info("Player with id " + playerId + " has stopped searching");
    }

    @GetMapping("/processing/{id}")
    public TetrisDto getOneInProcess(@PathVariable Long id) {
        return fromGameInfoToDto((TetrisGameInfo) gameKeeper.getById(id).orElseThrow(() -> new NotFoundException("Игра не найдена")));
    }

    @Scheduled(fixedRate = 25)
    private void gameProcessing() {
        currentIterationTime = LocalDateTime.now();
        gameKeeper.forEach((g) -> {
            TetrisGameInfo game = (TetrisGameInfo) g;
            updateTetrisLogic(game.getTetris1());
            updateTetrisLogic(game.getTetris2());
            updateNextFigures(game.getTetris1(), game.getTetris2());
            messagingTemplate.convertAndSend("/topic/tetris_game/" + game.getGameId(), fromGameInfoToDto(game));
            if (!isActiveTetrisGame(game.getTetris1()) && !isActiveTetrisGame(game.getTetris2())) {
                tetrisService.saveGame(game);
            }
        });
        Iterator<StorableGame> iterator = gameKeeper.iterator();
        while(iterator.hasNext()){
            TetrisGameInfo tGameInfo = (TetrisGameInfo) iterator.next();
            if(!isActiveTetrisGame(tGameInfo.getTetris1()) && !isActiveTetrisGame(tGameInfo.getTetris2())){
                iterator.remove();
            }
        }
    }

    @PostMapping("/move")
    public void makeMove(@RequestParam("player_id") Long playerId,
                         @RequestParam("game_id") Long gameId,
                         @RequestParam("move_code") Integer moveCode) {
        TetrisGameInfo gameInfo = (TetrisGameInfo) gameKeeper.getById(gameId).orElseThrow(()->{throw new NotFoundException("Game not found");});
        if (gameInfo.getPlayer1().getId().equals(playerId)) {
            gameInfo.getTetris1().move(moveCode);
        } else {
            gameInfo.getTetris2().move(moveCode);
        }
    }

    @PostMapping("/surrender")
    public void surrender(@RequestParam("game_id") Long gameId,
                          @RequestParam("player_id") Long playerId) {
        TetrisGameInfo game = (TetrisGameInfo) gameKeeper.getById(gameId).orElseThrow(NotFoundException::new);
        gameKeeper.removeById(gameId);

        game.getTetris1().setLose(true);
        if (game.getTetris2() != null) {
            game.getTetris2().setLose(true);
        }

        if (game.getPlayer1().getId().equals(playerId)) {
            game.getTetris1().setTimeInMillis(-game.getTetris1().getTimeInMillis());
            game.setWinner(game.getPlayer2());
        } else if (game.getPlayer2().getId().equals(playerId)) {
            game.getTetris2().setTimeInMillis(-game.getTetris2().getTimeInMillis());
            game.setWinner(game.getPlayer1());
        } else {
            throw new BadRequestException();
        }
        tetrisService.saveGame(game);
        messagingTemplate.convertAndSend("/topic/tetris_game/" + game.getGameId(),
                fromGameInfoToDto(game));
    }

    private boolean isActiveTetrisGame(TetrisLogic tetris) {
        return tetris != null && !tetris.isLose();
    }

    private void updateTetrisLogic(TetrisLogic tetris) {
        if (tetris == null) return;
        tetris.addTime(loopTime);
        if (ChronoUnit.MILLIS.between(tetris.getLastIterationTime(), currentIterationTime) >= (800 - 50L * tetris.getSpeed())) {
            tetris.next();
        }
    }

    private void updateNextFigures(TetrisLogic tetris1, TetrisLogic tetris2) {
        if (tetris1.needFigures() || tetris2 != null && tetris2.needFigures()) {
            List<int[][]> figures = TetrisFigureUtils.generateFigures(countNextFigures);
            tetris1.getField().addFigures(figures);
            if (tetris2 != null) {
                tetris2.getField().addFigures(figures);
            }
        }
    }

    private TetrisDto fromGameInfoToDto(TetrisGameInfo gameInfo) {
        return TetrisDto.builder()
                .field1(gameInfo.getTetris1().getField().getField())
                .field2(gameInfo.getTetris2() == null ? null : gameInfo.getTetris2().getField().getField())
                .lastRemovedRows1(gameInfo.getTetris1().getLastRemovedRowsNumbers())
                .lastRemovedRows2(gameInfo.getTetris2() == null ? null : gameInfo.getTetris2().getLastRemovedRowsNumbers())
                .player1Points(gameInfo.getTetris1().getPoints())
                .player2Points(gameInfo.getTetris2() == null ? null : gameInfo.getTetris2().getPoints())
                .player1Time(gameInfo.getTetris1().getTimeInMillis())
                .player2Time(gameInfo.getTetris2() == null ? null : gameInfo.getTetris2().getTimeInMillis())
                .winner(gameInfo.getWinner())
                .player1(gameInfo.getPlayer1())
                .player2(gameInfo.getPlayer2())
                .nextFigure1(gameInfo.getTetris1().getField().nextFigure())
                .nextFigure2(gameInfo.getTetris2() == null ? null : gameInfo.getTetris2().getField().nextFigure())
                .game1Stop(gameInfo.getTetris1().isEnd())
                .game2Stop(gameInfo.getTetris2() == null ? false : gameInfo.getTetris2().isEnd())
                .gameId(gameInfo.getGameId())
                .build();
    }


}

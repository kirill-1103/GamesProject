package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.TetrisGameJdbcTemplate;
import ru.krey.games.domain.games.tetris.TetrisGame;
import ru.krey.games.domain.games.tetris.TetrisGameInfo;
import ru.krey.games.dto.TetrisDto;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.dto.TttSearchDto;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.logic.tetris.TetrisField;
import ru.krey.games.logic.tetris.TetrisFigureUtils;
import ru.krey.games.logic.tetris.TetrisLogic;
import ru.krey.games.service.TetrisService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/*
    TODO: в игре с человеком время игры и следующие фигуры устанавливаются из контроллера
    в классе  figureutils реализована генерация фигур. за ними надо следить и дополнять при необходимости
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tetris_game")
public class TetrisGameController {
    private final TetrisService tetrisService;

    private final Set<TetrisGameInfo> savedGames = new ConcurrentSkipListSet<>(Comparator.comparingLong(TetrisGameInfo::getGameId));

    private final Deque<TttSearchDto> searches = new ArrayDeque<>();

    private final int countNextFigures = 20;

    private final int loopTime = 50;

    private LocalDateTime currentIterationTime;

    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping("/computer")
    public TetrisDto newGame(
            @RequestParam("player_id") Long playerId,
            @RequestParam(value = "field_width", required = false) Integer fieldWidth,
            @RequestParam(value = "field_height", required = false) Integer fieldHeight
            ){
        TetrisGame newGame = tetrisService.newGame(playerId,null);
        TetrisLogic logic1 = new TetrisLogic(new TetrisField(TetrisFigureUtils.generateFigures(countNextFigures)));
//        TetrisLogic logic2 = new TetrisLogic(new TetrisField(logic1.getField().getNextFigures()));

        TetrisGameInfo gameInfo = TetrisGameInfo.builder()
                .gameId(newGame.getId())
                .tetris1(logic1)
                .player1(newGame.getPlayer1())
                .build();
        savedGames.add(gameInfo);

        return TetrisDto.builder()
                .game2Stop(false)
                .game1Stop(false)
                .field1(gameInfo.getTetris1().getField().getField())
                .gameId(newGame.getId())
                .build();
    }

    @GetMapping("/processing/{id}")
    public TetrisDto getOneInProcess(@PathVariable Long id){
        return fromGameInfoToDto(savedGames.stream().filter(g->g.getGameId().equals(id))
                .findAny().orElseThrow(()->new NotFoundException("Игра не найдена")));
    }

    @Scheduled(fixedRate = 1000)
    private void gameProcessing(){
        currentIterationTime = LocalDateTime.now();
        savedGames.forEach((game)->{
            updateTetrisLogic(game.getTetris1());
            updateTetrisLogic(game.getTetris2());
            messagingTemplate.convertAndSend("/topic/tetris_game/" + game.getGameId(), fromGameInfoToDto(game));
        });
    }

    private void updateTetrisLogic(TetrisLogic tetris){
        if(tetris == null) return;
        tetris.addTime(loopTime);
        if(ChronoUnit.MILLIS.between( tetris.getLastIterationTime(),currentIterationTime)>=(1500 - 100L *tetris.getSpeed())){
            tetris.next();
        }
    }

    private TetrisDto fromGameInfoToDto(TetrisGameInfo gameInfo){
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

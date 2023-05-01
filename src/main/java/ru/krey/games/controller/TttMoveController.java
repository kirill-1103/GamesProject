package ru.krey.games.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.games.ttt.TttGame;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.dto.TttMoveDto;
import ru.krey.games.error.NotFoundException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/ttt_move")
public class TttMoveController {
    private final PlayerDao playerDao;

    private final TttGameDao gameDao;

    private final ConversionService conversionService;

    private final static Logger log = LoggerFactory.getLogger(TttMoveController.class);

    private final TttMoveDao moveDao;

    @AllArgsConstructor
    @Getter
    private static class GameWithMoves{
        private  TttGameDto game;
        private  List<TttMoveDto> moves;
    }

    @PostMapping("/all")
    public @ResponseBody GameWithMoves getGameWithMoves(@RequestParam("id") Long gameId){
        TttGame game = gameDao.getOneById(gameId).orElseThrow(NotFoundException::new);
        List<TttMoveDto> moves = moveDao.getAllByGameIdOrderedByTime(gameId);
        GameWithMoves gameWithMoves = new GameWithMoves(conversionService.convert(game, TttGameDto.class), moves);
        return gameWithMoves;
    }

}

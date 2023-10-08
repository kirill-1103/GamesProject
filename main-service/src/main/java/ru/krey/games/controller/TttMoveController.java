package ru.krey.games.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.domain.games.ttt.TttGame;
import ru.krey.games.dto.TttGameDto;
import ru.krey.games.dto.TttMoveDto;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.TttGameService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/ttt_move")
public class TttMoveController {

    private final TttGameService tttGameService;
    private final ConversionService conversionService;

    @AllArgsConstructor
    @Getter
    private static class GameWithMoves{
        private  TttGameDto game;
        private  List<TttMoveDto> moves;
    }

    @PostMapping("/all")
    public @ResponseBody GameWithMoves getGameWithMoves(@RequestParam("id") Long gameId){
        TttGame game = tttGameService.getOneById(gameId).orElseThrow(NotFoundException::new);
        List<TttMoveDto> moves = tttGameService.getGameMoves(game);
        GameWithMoves gameWithMoves = new GameWithMoves(conversionService.convert(game, TttGameDto.class), moves);
        return gameWithMoves;
    }
}

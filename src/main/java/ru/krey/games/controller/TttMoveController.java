package ru.krey.games.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.TttMove;
import ru.krey.games.dto.TttMoveDto;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor

public class TttMoveController {

    private final PlayerDao playerDao;
    private final TttGameDao gameDao;

    private final ConversionService conversionService;

    private final static Logger log = LoggerFactory.getLogger(TttMoveController.class);

    private final TttMoveDao moveDao;


    @MessageMapping({"/api/ttt_move/temp"} )
    @SendTo("/topic/ttt_move")
    public @ResponseBody TttMoveDto temp(@RequestBody TttMoveDto moveDto){
        TttMove move = TttMove.builder()
                .absoluteTime(LocalDateTime.now())
                .player(playerDao.getOneById(moveDto.getPlayerId()).get())
                .game(gameDao.getOneById(moveDto.getGameId()).get())
                .xCoordinate(moveDto.getXCoord())
                .yCoordinate(moveDto.getYCoord())
                .gameTimeMillis(3)
                .build();
        log.debug(move.toString());
        TttMove moveFromDb =  moveDao.saveOrUpdate(move);
        return conversionService.convert(moveFromDb,TttMoveDto.class);
    }
}

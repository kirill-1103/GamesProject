package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.games.ttt.TttMove;
import ru.krey.games.dto.TttMoveDto;
import ru.krey.games.error.BadRequestException;

@RequiredArgsConstructor
@Component
public class TttMoveDtoToTttMoveConverter implements Converter<TttMoveDto,TttMove> {

    private final PlayerDao playerDao;
    private final TttGameDao gameDao;

    @Override
    public TttMove convert(TttMoveDto tttMoveDto) {
        return TttMove.builder()
                .id(tttMoveDto.getId())
                .player(playerDao.getOneById(tttMoveDto.getPlayerId())
                        .orElseThrow(BadRequestException::new))
                .xCoordinate(tttMoveDto.getXCoord())
                .yCoordinate(tttMoveDto.getYCoord())
                .gameTimeMillis(tttMoveDto.getGameTimeMillis())
                .absoluteTime(tttMoveDto.getAbsoluteTime())
                .game(gameDao.getOneById(tttMoveDto.getGameId())
                        .orElseThrow(BadRequestException::new))
                .build();
    }
}

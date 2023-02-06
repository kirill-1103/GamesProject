package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.TttMove;
import ru.krey.games.dto.TttMoveDto;

@Component
@RequiredArgsConstructor
public class TttMoveToTttMoveDtoConverter implements Converter<TttMove, TttMoveDto> {

    @Override
    public TttMoveDto convert(TttMove move) {
        return TttMoveDto.builder()
                .id(move.getId())
                .playerId(move.getPlayer().getId())
                .gameId(move.getGame().getId())
                .gameTimeMillis(move.getGameTimeMillis())
                .xCoord(move.getXCoordinate())
                .yCoord(move.getYCoordinate())
                .absoluteTime(move.getAbsoluteTime())
                .build();
    }
}

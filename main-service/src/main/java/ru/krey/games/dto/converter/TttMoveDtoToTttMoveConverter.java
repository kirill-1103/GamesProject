package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.games.ttt.TttMove;
import ru.krey.games.dto.TttMoveDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.service.PlayerService;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class TttMoveDtoToTttMoveConverter implements Converter<TttMoveDto,TttMove> {

    private final PlayerService playerService;
    private final TttGameDao gameDao;

    @Override
    public TttMove convert(TttMoveDto tttMoveDto) {
          TttMove tttMove = TttMove.builder()
                .id(tttMoveDto.getId())
                .xCoordinate(tttMoveDto.getXCoord())
                .yCoordinate(tttMoveDto.getYCoord())
                .gameTimeMillis(tttMoveDto.getGameTimeMillis())
                .absoluteTime(tttMoveDto.getAbsoluteTime())
                .game(gameDao.getOneById(tttMoveDto.getGameId())
                        .orElseThrow(BadRequestException::new))
                .build();
          Player player = playerService.getOneByIdOrNull(tttMoveDto.getPlayerId());
          if(Objects.isNull(player)){
              throw new BadRequestException();
          }
          tttMove.setPlayer(player);
          return tttMove;
    }
}

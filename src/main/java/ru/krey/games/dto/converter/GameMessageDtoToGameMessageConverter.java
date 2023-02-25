package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.dto.GameMessageDto;


@RequiredArgsConstructor
@Component
public class GameMessageDtoToGameMessageConverter implements Converter<GameMessageDto, GameMessage> {

    private final PlayerDao playerDao;

    @Override
    public GameMessage convert(GameMessageDto dto) {
        return GameMessage.builder()
                .gameId(dto.getGameId())
                .gameCode(dto.getGameCode())
                .id(dto.getId())
                .message(dto.getMessage())
                .sender(playerDao.getOneById(dto.getSenderId()).orElseThrow(RuntimeException::new))
                .time(dto.getTime())
                .build();
    }
}

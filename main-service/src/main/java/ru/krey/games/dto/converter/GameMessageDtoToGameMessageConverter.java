package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.domain.Player;
import ru.krey.games.dto.GameMessageDto;
import ru.krey.games.service.PlayerService;


@RequiredArgsConstructor
@Component
public class GameMessageDtoToGameMessageConverter implements Converter<GameMessageDto, GameMessage> {

    private final PlayerService playerService;

    @Override
    public GameMessage convert(GameMessageDto dto) {
        GameMessage gameMessage = GameMessage.builder()
                .gameId(dto.getGameId())
                .gameCode(dto.getGameCode())
                .id(dto.getId())
                .message(dto.getMessage())
                .time(dto.getTime())
                .build();
        Player player = playerService.getOneByIdOrNull(dto.getSenderId());
        if(player == null){
            throw new RuntimeException();
        }
        gameMessage.setSender(player);
        return gameMessage;
    }
}

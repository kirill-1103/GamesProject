package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.dto.GameMessageDto;

@RequiredArgsConstructor
@Component
public class GameMessageToDtoConverter implements Converter<GameMessage, GameMessageDto> {

    @Override
    public GameMessageDto convert(GameMessage message) {
        return GameMessageDto.builder()
                .id(message.getId())
                .message(message.getMessage())
                .gameId(message.getGameId())
                .gameCode(message.getGameCode())
                .senderId(message.getSender().getId())
                .time(message.getTime())
                .build();
    }
}

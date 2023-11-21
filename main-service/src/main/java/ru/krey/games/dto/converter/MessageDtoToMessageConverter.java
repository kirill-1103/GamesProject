package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.Message;
import ru.krey.games.dto.MessageDto;
import ru.krey.games.service.PlayerService;

@Component
@RequiredArgsConstructor
public class MessageDtoToMessageConverter implements Converter<MessageDto, Message>{

    private final PlayerService playerService;

    @Override
    public Message convert(MessageDto message) {
        return Message.builder()
                .id(message.getId())
                .sendingTime(message.getSendingTime())
                .recipient(playerService.getOneByIdOpt(message.getRecipientId()).
                        orElseThrow(()->new IllegalArgumentException("Player with id not found:"+message.getRecipientId())))
                .sender(playerService.getOneByIdOpt(message.getSenderId())
                        .orElseThrow(()->new IllegalArgumentException("Player with id not found:"+message.getSenderId())))
                .messageText(message.getMessageText())
                .readingTime(message.getReadingTime())
                .build();
    }
}

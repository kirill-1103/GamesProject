package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Message;
import ru.krey.games.dto.MessageDto;
import ru.krey.games.error.BadRequestException;

@Component
@RequiredArgsConstructor
public class MessageDtoToMessageConverter implements Converter<MessageDto, Message>{

    private final PlayerDao playerDao;

    @Override
    public Message convert(MessageDto message) {
        return Message.builder()
                .id(message.getId())
                .sendingTime(message.getSendingTime())
                .recipient(playerDao.getOneById(message.getRecipientId()).
                        orElseThrow(()->new IllegalArgumentException("Player with id not found:"+message.getRecipientId())))
                .sender(playerDao.getOneById(message.getSenderId())
                        .orElseThrow(()->new IllegalArgumentException("Player with id not found:"+message.getSenderId())))
                .messageText(message.getMessageText())
                .readingTime(message.getReadingTime())
                .build();
    }
}

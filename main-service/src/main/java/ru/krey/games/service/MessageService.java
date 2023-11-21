package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.dao.interfaces.MessageDao;
import ru.krey.games.domain.Message;
import ru.krey.games.mapper.PlayerMapper;
import ru.krey.games.openapi.model.PlayerOpenApi;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final PlayerService playerService;
    private final MessageDao messageDao;
    private final PlayerMapper playerMapper;

    public List<Message> getLastMessagesByPlayerId(Long playerId) {
        List<Message> messages = messageDao.getLastMessagesByPlayerId(playerId);
        setPlayersInMessages(messages);
        return messages;
    }

    public List<Message> getAllMessagesBetweenPlayers(Long id1, Long id2) {
        List<Message> messages = messageDao.getAllMessagesBetweenPlayers(id1, id2);
        setPlayersInMessages(messages);
        return messages;
    }

    public List<Message> getAllMessagesByPlayerId(Long id) {
        List<Message> messages = messageDao.getAllMessagesByPlayerId(id);
        setPlayersInMessages(messages);
        return messages;
    }

    public Message saveOrUpdate(Message message) {
        if(Objects.isNull(message.getSender()) && Objects.nonNull(message.getSenderId())){
            message.setSender(playerService.getOneByIdOrNull(message.getSenderId()));
        }
        if(Objects.isNull(message.getRecipient()) && Objects.nonNull(message.getRecipientId())){
            message.setRecipient(playerService.getOneByIdOrNull(message.getRecipientId()));
        }
        return messageDao.saveOrUpdate(message);
    }

    public void updateReadingTime(List<Long> ids) {
        messageDao.updateReadingTime(ids);
    }

    public void setPlayersInMessages(List<Message> messagesWithoutPlayers){
        Map<Long,PlayerOpenApi> idToPlayerMap = playerService.getPlayersByIds(
                messagesWithoutPlayers.stream()
                        .flatMap(message -> Stream.of(message.getSenderId(), message.getRecipientId()))
                        .collect(Collectors.toSet())
        );
        messagesWithoutPlayers.forEach(message->{
            message.setSender(playerMapper.toPlayer(idToPlayerMap.get(message.getSenderId())));
            message.setRecipient(playerMapper.toPlayer(idToPlayerMap.get(message.getRecipientId())));
        });
    }
}

package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.dao.interfaces.MessageDao;
import ru.krey.games.domain.Message;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final PlayerService playerService;
    private final MessageDao messageDao;

    public List<Message> getLastMessagesByPlayerId(Long playerId){
        return messageDao.getLastMessagesByPlayerId(playerId);
    }

    public List<Message> getAllMessagesBetweenPlayers(Long id1, Long id2){
        return messageDao.getAllMessagesBetweenPlayers(id1,id2);
    }

    public List<Message> getAllMessagesByPlayerId(Long id){
        return messageDao.getAllMessagesByPlayerId(id);
    }

    public Message saveOrUpdate(Message message){
        return messageDao.saveOrUpdate(message);
    }

    public void updateReadingTime(List<Long> ids){
        messageDao.updateReadingTime(ids);
    }
}

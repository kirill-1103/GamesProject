package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.Message;

import java.util.List;

public interface MessageDao {
    List<Message> getAllMessagesByPlayerId(Long id);

    List<Message> getLastMessagesByPlayerId(Long id);

    List<Message> getAllMessagesBetweenPlayers(Long player1Id, Long player2Id);


    List<Message> getAll();

    Message saveOrUpdate(Message message);

    void updateReadingTime(List<Long> ids);

}

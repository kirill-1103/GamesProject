package ru.krey.games.dao.interfaces;

import ru.krey.games.domain.Message;

import java.util.List;

public interface MessageDao {
    List<Message> getAllMessagesByPlayerId(Long id);

    List<Message> getLastMessagesByPlayerId(Long id);


    List<Message> getAll();

    Message saveOrUpdate(Message message);
}

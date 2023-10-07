package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.dao.interfaces.GameMessageDao;
import ru.krey.games.dao.interfaces.MessageDao;
import ru.krey.games.domain.GameMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameMessageService {

    private final GameMessageDao gameMessageDao;

    public List<GameMessage> getMessagesByGameIdAndCode(Long gameId, Integer gameCode){
        return gameMessageDao.getAllByGameIdAndGameCode(gameId, gameCode);
    }

    public GameMessage saveOrUpdate(GameMessage gameMessage){
        return gameMessageDao.saveOrUpdate(gameMessage);
    }
}

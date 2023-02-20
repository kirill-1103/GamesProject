package ru.krey.games.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.domain.Player;
import ru.krey.games.error.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class GameMessageMapper implements RowMapper<GameMessage> {
    private final PlayerDao playerDao;

    @Override
    public GameMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GameMessage.builder()
                .id(rs.getLong("id"))
                .gameId(rs.getLong("game_id"))
                .gameCode(rs.getInt("game_code"))
                .sender(getPlayerById(rs.getLong("sender_id")))
                .time(rs.getTimestamp("time").toLocalDateTime())
                .message(rs.getString("message"))
                .build();
    }

    private Player getPlayerById(Long id){
        return playerDao.getOneById(id)
                .orElseThrow(()->new NotFoundException("Player must exist in this context"));
    }

}

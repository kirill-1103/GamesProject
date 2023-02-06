package ru.krey.games.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;
import ru.krey.games.domain.TttMove;
import ru.krey.games.error.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@RequiredArgsConstructor
@Component
public class TttMoveMapper implements RowMapper<TttMove> {
    private final PlayerDao playerDao;
    private final TttGameDao gameDao;

    @Override
    public TttMove mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TttMove.builder()
                .id(rs.getLong("id"))
                .absoluteTime(rs.getTimestamp("absolute_time").toLocalDateTime())
                .gameTimeMillis(rs.getInt("game_time_millis"))
                .xCoordinate(rs.getInt("x_coordinate"))
                .yCoordinate(rs.getInt("y_coordinate"))
                .player(getPlayerById(rs.getLong("player_id")))
                .game(getGameById(rs.getLong("game_id")))
                .build();
    }

    private Player getPlayerById(Long id) {
        return playerDao.getOneById(id).orElse(null);
    }

    private TttGame getGameById(Long id) {
        return gameDao.getOneById(id).
                orElseThrow(()->new NotFoundException("Game must exist in this context"));
    }
}

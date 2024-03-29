package ru.krey.games.utils.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.games.ttt.TttGame;
import ru.krey.games.domain.games.ttt.TttMove;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.PlayerService;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Component
public class TttMoveMapper implements RowMapper<TttMove> {
    private final PlayerService playerService;
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

        return playerService.getOneByIdOrNull(id);
    }

    private TttGame getGameById(Long id) {
        return gameDao.getOneById(id).
                orElseThrow(()->new NotFoundException("Game must exist in this context"));
    }
}

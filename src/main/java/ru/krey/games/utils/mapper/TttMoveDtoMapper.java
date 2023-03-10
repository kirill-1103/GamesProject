package ru.krey.games.utils.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.TttMove;
import ru.krey.games.dto.TttMoveDto;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TttMoveDtoMapper implements RowMapper<TttMoveDto> {

    @Override
    public TttMoveDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TttMoveDto.builder()
                .id(rs.getLong("id"))
                .absoluteTime(rs.getTimestamp("absolute_time").toLocalDateTime())
                .gameTimeMillis(rs.getInt("game_time_millis"))
                .xCoord(rs.getInt("x_coordinate"))
                .yCoord(rs.getInt("y_coordinate"))
                .playerId(rs.getLong("player_id"))
                .gameId(rs.getLong("game_id"))
                .build();
    }
}

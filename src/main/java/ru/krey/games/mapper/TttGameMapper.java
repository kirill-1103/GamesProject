package ru.krey.games.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class TttGameMapper implements RowMapper<TttGame> {

    private final PlayerDao playerDao;

    @Override
    public TttGame mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TttGame.builder()
                .id(rs.getLong("id"))
                .player1(getPlayerById(rs.getLong("player1_id")))
                .player2(getPlayerById(rs.getLong("player2_id")))
                .startTime(rs.getTimestamp("start_time").toLocalDateTime())
                .endTime(rs.getTimestamp("end_time").toLocalDateTime())
                .winner(getPlayerById(rs.getLong("winner_id")))
                .sizeField(rs.getInt("size_field"))
                .baseDuration(rs.getInt("base_duration"))
                .actualDuration(rs.getInt("actual_duration"))
                .victoryReasonCode(rs.getByte("victory_reason_code"))
                .build();

    }

    private Player getPlayerById(Long id){
        return playerDao.getOneById(id).orElse(null);
    }
}

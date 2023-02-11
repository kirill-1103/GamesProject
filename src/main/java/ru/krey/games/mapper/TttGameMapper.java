package ru.krey.games.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class TttGameMapper implements RowMapper<TttGame> {

    private final PlayerDao playerDao;

    @Override
    public TttGame mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDateTime end_time = rs.getTimestamp("end_time") == null ? null : rs.getTimestamp("end_time").toLocalDateTime();

        return TttGame.builder()
                .id(rs.getLong("id"))
                .player1(getPlayerById(rs.getLong("player1_id")))
                .player2(getPlayerById(rs.getLong("player2_id")))
                .startTime(rs.getTimestamp("start_time").toLocalDateTime())
                .endTime(end_time)
                .winner(getPlayerById(rs.getLong("winner_id")))
                .sizeField(rs.getInt("size_field"))
                .player1Time(rs.getLong("player1_time"))
                .player2Time(rs.getLong("player2_time"))
                .basePlayerTime(rs.getLong("base_player_time"))
                .actualDuration(rs.getInt("actual_duration"))
                .victoryReasonCode(rs.getByte("victory_reason_code"))
                .complexity(rs.getInt("complexity"))
                .queue(rs.getByte("queue"))
                .build();

    }

    private Player getPlayerById(Long id){
        return playerDao.getOneById(id).orElse(null);
    }
}

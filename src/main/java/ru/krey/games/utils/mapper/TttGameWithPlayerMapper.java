package ru.krey.games.utils.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class TttGameWithPlayerMapper implements RowMapper<TttGame> {
    @Override
    public TttGame mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDateTime end_time = rs.getTimestamp("end_time") == null ? null : rs.getTimestamp("end_time").toLocalDateTime();

        Player player1 = getPlayerFromRsByPrefix(rs, "p1_");
        Player player2 = getPlayerFromRsByPrefix(rs, "p2_");
        Player winner;
        if(rs.getObject("winner_id") == null){
            winner = null;
        }else if(rs.getLong("winner_id") == player1.getId()){
            winner = player1;
        }else{
            winner = player2;
        }
        return TttGame.builder()
                .id(rs.getLong("id"))
                .player1(player1)
                .player2(player2)
                .startTime(rs.getTimestamp("start_time").toLocalDateTime())
                .endTime(end_time)
                .winner(winner)
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

    private Player getPlayerFromRsByPrefix(ResultSet rs, String prefix) throws SQLException {
        if (rs.getObject(prefix + "id") == null) {
            return null;
        }
        return Player.builder()
                .id(rs.getLong(prefix + "id"))
                .password(rs.getString(prefix + "password"))
                .login(rs.getString(prefix + "login"))
                .email(rs.getString(prefix + "email"))
                .enabled(rs.getBoolean(prefix + "enabled"))
                .photo(rs.getString(prefix + "photo"))
                .rating(rs.getInt(prefix + "rating"))
                .lastSignInTime(rs.getTimestamp(prefix + "last_sign_in_time").toLocalDateTime())
                .signUpTime(rs.getTimestamp(prefix + "sign_up_time").toLocalDateTime())
                .Role(rs.getString(prefix + "role"))
                .lastGameCode(rs.getInt(prefix + "last_game_code"))
                .build();
    }
}

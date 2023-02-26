package ru.krey.games.utils.mapper;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PlayerMapper implements RowMapper<Player> {
    @Override
    public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Player.builder()
                .id(rs.getLong("id"))
                .password(rs.getString("password"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .enabled(rs.getBoolean("enabled"))
                .photo(rs.getString("photo"))
                .rating(rs.getInt("rating"))
                .lastSignInTime(rs.getTimestamp("last_sign_in_time").toLocalDateTime())
                .signUpTime(rs.getTimestamp("sign_up_time").toLocalDateTime())
                .Role(rs.getString("role"))
                .lastGameCode(rs.getInt("last_game_code"))
                .build();
    }
}

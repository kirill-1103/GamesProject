package ru.krey.games.utils.mapper;

import ru.krey.games.domain.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapperUtils {
    public static Player getPlayerFromRsByPrefix(ResultSet rs, String prefix) throws SQLException {
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
                .role(rs.getString(prefix + "role"))
                .lastGameCode(rs.getInt(prefix + "last_game_code"))
                .build();
    }
}

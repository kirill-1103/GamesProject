package ru.krey.games.playerservice.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.krey.games.playerservice.dao.interfaces.FriendsDao;
import ru.krey.games.playerservice.domain.Player;
import ru.krey.games.playerservice.utils.mapper.PlayerRowMapper;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class FriendsJdbcTemplate implements FriendsDao {

    private final PlayerRowMapper playerRowMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Player> getFriends(Long playerId, Long from, Long to) {
        if(playerId == null) throw new IllegalArgumentException("Player id is null");
        if(from == null || to == null) throw new IllegalArgumentException("from or to  == null");

        String query = "SELECT p.* FROM player p " +
                "INNER JOIN friends f ON p.id = f.id1 OR p.id = f.id2 " +
                "WHERE ? in (f.id1, f.id2) " +
                "ORDER BY f.friend_time DESC " +
                "OFFSET ? LIMIT ? ";

        return jdbcTemplate.query(query, playerRowMapper, playerId, from, to-from);
    }

    @Override
    public void removeFriend(Long playerId, Long friendId) {
        if(playerId == null || friendId == null) throw new IllegalArgumentException("id is null");
        String sql = "DELETE FROM friends f "  +
                "WHERE f.id1 = ? AND f.id2 = ? OR f.id2 = ? AND f.id1 = ?";
        jdbcTemplate.update(sql, playerId, friendId, playerId, friendId);
    }

    @Override
    public boolean friendAlreadyExists(Long playerId, Long friendId) {
        if(playerId == null || friendId == null) throw new IllegalArgumentException("id is null");
        String query = "SELECT * FROM friends f " +
                "WHERE ? in (f.id1, f.id2) AND ? in (f.id1, f.id2)";
        return !jdbcTemplate.query(query,(rs,rowNum) -> rs.getLong(0), playerId, friendId).isEmpty();
    }

    @Override
    public void addFriend(Long playerId, Long friendId) {
        if(playerId == null || friendId == null) throw new IllegalArgumentException("id is null");
        String sql = "INSERT INTO friends (id1, id2, friend_time) " +
                "VALUES(?,?,?)";
        jdbcTemplate.update(sql,playerId, friendId, Timestamp.valueOf(LocalDateTime.now()));
    }
}

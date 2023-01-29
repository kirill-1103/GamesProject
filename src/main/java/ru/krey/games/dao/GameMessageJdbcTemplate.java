package ru.krey.games.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.GameMessageDao;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.mapper.GameMessageMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameMessageJdbcTemplate implements GameMessageDao {
    private final JdbcTemplate jdbcTemplate;
    private final GameMessageMapper gameMessageMapper;

    private final static Logger log = LoggerFactory.getLogger(GameMessageJdbcTemplate.class);

    @Override
    public Optional<GameMessage> getOneById(Long id) {
        String query = "SELECT * FROM game_message WHERE id=?";
        return jdbcTemplate.query(query, gameMessageMapper, id).stream().findAny();
    }

    @Override
    public GameMessage saveOrUpdate(GameMessage message) {
        if (message == null) throw new IllegalArgumentException("GameMessage object is null");

        Long senderId = message.getSender() == null ? null : message.getSender().getId();

        if (message.getId() != null && getOneById(message.getId()).isPresent()) {
            /*update*/
            log.info("Update GameMessage: " + message);

            String query = "UPDATE game_message SET game_id=?,game_code=?,sender_id=?,time=?,message=? WHERE id=?";
            int rows = jdbcTemplate.update(query, message.getGameId(), message.getGameCode(),
                    senderId, message.getTime(), message.getMessage(), message.getId());
            if (rows!=1){
                throw new RuntimeException("Invalid request in sql: "+query);
            }
            return message;
        } else {
            /*save*/
            log.info("Save GameMessage: " + message);

            String query = "INSERT INTO game_message (game_id,game_code,sender_id,time,message)" +
                    " VALUES(?,?,?,?,?) RETURNING id";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                ps.setLong(index++, message.getGameId());
                ps.setInt(index++, message.getGameCode());
                ps.setLong(index++, senderId);
                ps.setTimestamp(index++, Timestamp.valueOf(message.getTime()));
                ps.setString(index++, message.getMessage());
                return ps;
            }, keyHolder);

            return getOneById(keyHolder.getKey().longValue())
                    .orElseThrow(() -> new RuntimeException("GameMessage must exist in this context"));

        }
    }

    @Override
    public Set<GameMessage> getAll() {
        String query = "SELECT * FROM game_message";
        return new HashSet<>(jdbcTemplate.query(query, gameMessageMapper));
    }

    @Override
    public Set<GameMessage> getAllBySenderId(Long senderId) {
        String query = "SELECT * FROM game_message WHERE sender_id=?";
        return new HashSet<>(jdbcTemplate.query(query, gameMessageMapper, senderId));
    }
}

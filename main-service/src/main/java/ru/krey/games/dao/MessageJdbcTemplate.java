package ru.krey.games.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.MessageDao;
import ru.krey.games.domain.Message;
import ru.krey.games.utils.mapper.MessageMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageJdbcTemplate implements MessageDao {
    private static final Logger log = LoggerFactory.getLogger(MessageJdbcTemplate.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final MessageMapper mapper;

    private final static String getAllMessages = "SELECT m.* FROM message as m ";


    @Override
    public List<Message> getAllMessagesByPlayerId(Long id) {
        String condition = "WHERE m.sender_id = ? OR m.recipient_id = ?";
        String query = getAllMessages + condition;
        return new ArrayList<>(jdbcTemplate.query(query, mapper, id, id));
    }

    @Override
    public List<Message> getLastMessagesByPlayerId(Long id) {
        String condition = "WHERE (m.sender_id = ? OR m.recipient_id = ?) " +
                "AND m.sending_time = (" +
                "SELECT MAX(sending_time) FROM message " +
                "WHERE (sender_id = m.sender_id AND recipient_id = m.recipient_id) " +
                "OR (sender_id = m.recipient_id AND recipient_id = m.sender_id)" +
                ")";
        String query = getAllMessages + condition;
        return new ArrayList<>(jdbcTemplate.query(query, mapper, id, id));
    }

    @Override
    public List<Message> getAllMessagesBetweenPlayers(Long player1Id, Long player2Id) {
        String condition = "WHERE m.sender_id = ? AND m.recipient_id = ? OR m.sender_id = ? AND m.recipient_id = ? " +
                "ORDER BY m.sending_time";
        String query = getAllMessages + condition;
        return new ArrayList<>(jdbcTemplate.query(query, mapper, player1Id, player2Id, player2Id, player1Id));
    }

    @Override
    public List<Message> getAll() {
        String query = getAllMessages;
        return new ArrayList<>(jdbcTemplate.query(query, mapper));
    }

    @Override
    public Message saveOrUpdate(Message message) {
        if (message == null) throw new IllegalArgumentException("TttGame object is null");
        if (message.getId() != null) {
            /*update*/
            log.info("Update message: " + message);
            String query = "UPDATE message SET " +
                    "sender_id=?, recipient_id = ?, message_text = ?, sending_time = ?, reading_time = ? " +
                    "WHERE id = ?";

            int rows = jdbcTemplate.update(query, message.getSender().getId(), message.getRecipient().getId(),
                    message.getMessageText(), message.getSendingTime(),
                    message.getReadingTime(), message.getId());

            if (rows != 1) {
                throw new RuntimeException("Invalid request to sql:" + query);
            }
            return message;
        } else {
            /*save*/
            log.info("Save message: " + message);

            KeyHolder keyHolder = new GeneratedKeyHolder();
            String query = "INSERT INTO message (recipient_id, sender_id, message_text," +
                    "sending_time, reading_time) VALUES(?,?,?,?,?) RETURNING id";

            jdbcTemplate.update(connection -> {
                int index = 1;
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(index++, message.getRecipient().getId());
                ps.setLong(index++, message.getSender().getId());
                ps.setString(index++, message.getMessageText());
                ps.setTimestamp(index++, Timestamp.valueOf(message.getSendingTime()));
                if (message.getReadingTime() == null) {
                    ps.setObject(index++, message.getReadingTime());
                } else {
                    ps.setTimestamp(index++, Timestamp.valueOf(message.getReadingTime()));
                }
                return ps;
            }, keyHolder);
            message.setId(keyHolder.getKey().longValue());
            return message;
        }
    }

    @Override
    public void updateReadingTime(List<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }
        String sql = "UPDATE message SET reading_time = (:time) WHERE message.id in (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        parameters.addValue("time", Timestamp.valueOf(LocalDateTime.now()));
        namedParameterJdbcTemplate.update(sql, parameters);
        log.info("Messages reading_time updated. Ids: " + ids);
    }
}

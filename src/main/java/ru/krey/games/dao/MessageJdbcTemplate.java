package ru.krey.games.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.MessageDao;
import ru.krey.games.domain.Message;
import ru.krey.games.utils.mapper.MessageMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageJdbcTemplate implements MessageDao {
    private static final Logger log = LoggerFactory.getLogger(MessageJdbcTemplate.class);

    private final JdbcTemplate jdbcTemplate;

    private final MessageMapper mapper;

    private final static String getAllMessagesWithPlayersInfo = "SELECT m.*," +
            "p1.id AS p1_id, p1.last_game_code AS p1_last_game_code, p1.login AS p1_login, " +
            "p1.password AS p1_password, p1.email as p1_email, p1.enabled as p1_enabled, " +
            "p1.last_sign_in_time as p1_last_sign_in_time, p1.photo AS p1_photo, p1.rating AS p1_rating, " +
            "p1.role AS p1_role, p1.sign_up_time AS p1_sign_up_time, " +
            "p2.id AS p2_id, p2.last_game_code AS p2_last_game_code, p2.login AS p2_login, " +
            "p2.password AS p2_password, p2.email as p2_email, p2.enabled as p2_enabled, " +
            "p2.last_sign_in_time as p2_last_sign_in_time, p2.photo as p2_photo, p2.rating as p2_rating," +
            "p2.role as p2_role, p2.sign_up_time as p2_sign_up_time " +
            "FROM message as m " +
            "INNER JOIN player AS p1 ON m.sender_id = p1.id " +
            "LEFT OUTER JOIN player AS p2 ON m.recipient_id = p2.id ";

    @Override
    public List<Message> getAllMessagesByPlayerId(Long id) {
        String condition = "WHERE m.sender_id = ? OR m.recipient_id = ?";
        String query = getAllMessagesWithPlayersInfo + condition;
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
        String query = getAllMessagesWithPlayersInfo + condition;
        return new ArrayList<>(jdbcTemplate.query(query, mapper, id, id));
    }

    @Override
    public List<Message> getAllMessagesBetweenPlayers(Long player1Id, Long player2Id) {
        String condition = "WHERE m.sender_id = ? AND m.recipient_id = ? OR m.sender_id = ? AND m.recipient_id = ?";
        String query = getAllMessagesWithPlayersInfo + condition;
        return new ArrayList<>(jdbcTemplate.query(query, mapper, player1Id, player2Id, player1Id, player2Id));
    }

    @Override
    public List<Message> getAll() {
        String query = getAllMessagesWithPlayersInfo;
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
}

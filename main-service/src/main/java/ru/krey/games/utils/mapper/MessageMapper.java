package ru.krey.games.utils.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.Message;
import ru.krey.games.domain.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Player sender = MapperUtils.getPlayerFromRsByPrefix(rs,"p1_");
        Player recipient = MapperUtils.getPlayerFromRsByPrefix(rs,"p2_");
        LocalDateTime readingTime = rs.getTimestamp("reading_time") == null ? null : rs.getTimestamp("reading_time").toLocalDateTime();

        return Message.builder()
                .id(rs.getLong("id"))
                .sender(sender)
                .recipient(recipient)
                .sendingTime(rs.getTimestamp("sending_time").toLocalDateTime())
                .readingTime(readingTime)
                .messageText(rs.getString("message_text"))
                .build();
    }


}

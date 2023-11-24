package ru.krey.games.playerservice.utils.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.playerservice.domain.FriendRequest;
import ru.krey.games.playerservice.openapi.model.FriendResponseEnumOpenApi;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class FriendRequestRowMapper implements RowMapper<FriendRequest> {
    @Override
    public FriendRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FriendRequest.builder()
                .id(rs.getLong("id"))
                .requestDate(rs.getTimestamp("request_date").toLocalDateTime())
                .responseDate(rs.getTimestamp("response_date").toLocalDateTime())
                .response(FriendResponseEnumOpenApi.valueOf(rs.getString("response").toUpperCase()))
                .receiverId(rs.getLong("receiver_id"))
                .senderId(rs.getLong("sender_id"))
                .build();
    }
}

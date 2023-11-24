package ru.krey.games.playerservice.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.krey.games.playerservice.dao.interfaces.FriendRequestDao;
import ru.krey.games.playerservice.domain.FriendRequest;
import ru.krey.games.playerservice.utils.mapper.FriendRequestRowMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FriendRequestJdbcTemplate implements FriendRequestDao {

    private final JdbcTemplate jdbcTemplate;

    private final FriendRequestRowMapper friendRequestRowMapper;


    @Override
    public List<FriendRequest> getReceived(Long playerId) {
        String query = "SELECT fr.* FROM friend_request fr" +
                "WHERE fr.receiver_id = ?";
        return jdbcTemplate.query(query, friendRequestRowMapper, playerId);
    }

    @Override
    public List<FriendRequest> getSent(Long playerId) {
        String query = "SELECT fr.* FROM friend_request fr " +
                "WHERE fr.sender_id = ?";
        return jdbcTemplate.query(query, friendRequestRowMapper, playerId);
    }

    @Override
    public void save(FriendRequest request) {
        String sql = "INSERT INTO friend_request" +
                "(" +
                "request_date, " +
                "response_date, " +
                "sender_id, " +
                "receiver_id, " +
                "response" +
                ") " +
                "VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                Timestamp.valueOf(request.getRequestDate()),
                Timestamp.valueOf(request.getResponseDate()),
                request.getSenderId(),
                request.getReceiverId(),
                request.getResponse().getValue().toLowerCase());
    }

    @Override
    public Optional<FriendRequest> getById(Long requestId) {
        String query = "SELECT * FROM friend_request " +
                "WHERE id = ?";
        return jdbcTemplate.query(query,friendRequestRowMapper,requestId)
                .stream()
                .findFirst();
    }

    @Override
    public void update(FriendRequest friendRequest) {
        String sql = "UPDATE friend_request " +
                "SET" +
                "response = ?, " +
                "response_date = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                friendRequest.getResponse().getValue().toLowerCase(),
                Timestamp.valueOf(friendRequest.getResponseDate()));
    }
}

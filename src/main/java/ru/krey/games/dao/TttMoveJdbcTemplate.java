package ru.krey.games.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.TttMove;
import ru.krey.games.mapper.TttMoveMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TttMoveJdbcTemplate implements TttMoveDao {

    private final JdbcTemplate jdbcTemplate;
    private final TttMoveMapper moveMapper;

    private final static Logger log = LoggerFactory.getLogger(TttMoveJdbcTemplate.class);

    @Override
    public Optional<TttMove> getOneById(Long id) {
        String query = "SELECT * FROM ttt_move WHERE id=?";
        return jdbcTemplate.query(query, moveMapper, id)
                .stream().findAny();
    }

    @Override
    public Set<TttMove> getAll() {
        String query = "SELECT * FROM ttt_move";
        return new HashSet<>(jdbcTemplate.query(query, moveMapper));
    }

    @Override
    public Set<TttMove> getAllByPlayerId(Long playerId) {
        String query = "SELECT * FROM ttt_move WHERE player_id=?";
        return new HashSet<>(jdbcTemplate.query(query, moveMapper, playerId));
    }

    @Override
    public Set<TttMove> getAllByGameId(Long gameId) {
        String query = "SELECT * FROM ttt_move WHERE game_id=?";
        return new HashSet<>(jdbcTemplate.query(query, moveMapper, gameId));
    }

    @Override
    public TttMove saveOrUpdate(TttMove move) {
        if (move == null) throw new IllegalArgumentException("TttMove object is null");

        final Long playerId = move.getPlayer() == null ? null : move.getPlayer().getId();

        if (move.getId() != null && getOneById(move.getId()).isPresent()) {
            /*update*/
            log.info("Update TttMove: " + move);

            String query = "UPDATE ttt_move SET " +
                    "absolute_time=?, game_time_millis=?, x_coordinate=?,y_coordinate=?," +
                    "player_id=?,game_id=? WHERE id=?";
            int rows = jdbcTemplate.update(query, move.getAbsoluteTime(), move.getGameTimeMillis(),
                    move.getXCoordinate(), move.getYCoordinate(), playerId, move.getGame().getId(),
                    move.getId());
            if (rows != 1) {
                throw new RuntimeException("Invalid request to sql: " + query);
            }
            return move;
        } else {
            /*save*/
            log.info("Save TttMove: " + move);
            KeyHolder keyHolder = new GeneratedKeyHolder();

            String query = "INSERT INTO ttt_move (absolute_time, game_time_millis, " +
                    "x_coordinate, y_coordinate, player_id, game_id) " +
                    "VALUES(?,?,?,?,?,?) RETURNING id";

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                int index = 1;
                ps.setTimestamp(index++, Timestamp.valueOf(move.getAbsoluteTime()));
                ps.setLong(index++, move.getGameTimeMillis());
                ps.setInt(index++, move.getXCoordinate());
                ps.setInt(index++, move.getYCoordinate());
                ps.setLong(index++,playerId);
                ps.setLong(index, move.getGame().getId());
                return ps;
            },keyHolder);
            return getOneById(keyHolder.getKey().longValue())
                    .orElseThrow(() -> new RuntimeException("TttMove must exist in this context"));
        }
    }
}

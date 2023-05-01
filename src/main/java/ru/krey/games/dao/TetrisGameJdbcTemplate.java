package ru.krey.games.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.TetrisGameDao;
import ru.krey.games.domain.games.tetris.TetrisGame;
import ru.krey.games.logic.tetris.TetrisField;
import ru.krey.games.utils.mapper.TetrisGameMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TetrisGameJdbcTemplate implements TetrisGameDao {
    private final JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(TetrisGameJdbcTemplate.class);

    private final TetrisGameMapper gameMapper;

    private final String getAllGamesWithFullPlayersInfo =
            "SELECT  g.*," +
                    "p1.id AS p1_id, p1.last_game_code AS p1_last_game_code, p1.login AS p1_login, " +
                    "p1.password AS p1_password, p1.email as p1_email, p1.enabled as p1_enabled, " +
                    "p1.last_sign_in_time as p1_last_sign_in_time, p1.photo AS p1_photo, p1.rating AS p1_rating, " +
                    "p1.role AS p1_role, p1.sign_up_time AS p1_sign_up_time, " +
                    "p2.id AS p2_id, p2.last_game_code AS p2_last_game_code, p2.login AS p2_login, " +
                    "p2.password AS p2_password, p2.email as p2_email, p2.enabled as p2_enabled, " +
                    "p2.last_sign_in_time as p2_last_sign_in_time, p2.photo as p2_photo, p2.rating as p2_rating," +
                    "p2.role as p2_role, p2.sign_up_time as p2_sign_up_time " +
                    "FROM tetris_game AS g " +
                    "INNER JOIN player AS p1 ON g.player1_id = p1.id " +
                    "LEFT OUTER JOIN player AS p2 ON g.player2_id = p2.id ";

    @Override
    public TetrisGame saveOrUpdate(TetrisGame game)  {
        if (game == null) throw new IllegalArgumentException("Tetris object is null");

        final Long player2Id = game.getPlayer2() == null ? null : game.getPlayer2().getId();
        final Long winnerId = game.getWinner() == null ? null : game.getWinner().getId();

        if (game.getId() != null && getOneById(game.getId()).isPresent()) {

            String query = "UPDATE tetris_game SET " +
                    "player1_id=?, player2_id=?, start_time=?,end_time=?," +
                    "winner_id=?, field_1=?, field_2=?, player1_time=?,player2_time=?, player1_points = ?, player2_points= ?, " +
                    "duration =?,  WHERE id=? ";


            int rows = 0;
            try {
                rows = jdbcTemplate.update(query, game.getPlayer1().getId(), player2Id,
                        game.getStartTime(), game.getEndTime(), winnerId, TetrisField.toJson(game.getField1()),TetrisField.toJson(game.getField2()),
                        game.getPlayer1Time(), game.getPlayer2Time(), game.getPlayer1Points(), game.getPlayer2Points(),
                        game.getDuration(), game.getId());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Bad converting tetris field to json");
            }
            if (rows != 1) {
                throw new RuntimeException("Invalid request to sql: " + query);
            }
            return game;
        } else {
            /*save*/
            log.info("Save TetrisGame: " + game);

            KeyHolder keyHolder = new GeneratedKeyHolder();

            String query = "INSERT INTO tetris_game (player1_id,player2_id,start_time," +
                    "end_time,winner_id,field_1, field_2, player1_time,player2_time,player1_points, player2_points, duration)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id";

            jdbcTemplate.update(connection -> {
                int index = 1;
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(index++, game.getPlayer1().getId());
                if (player2Id == null) {
                    ps.setObject(index++, player2Id);
                } else {
                    ps.setLong(index++, player2Id);
                }
                ps.setTimestamp(index++, Timestamp.valueOf(game.getStartTime()));
                if (game.getEndTime() == null) {
                    ps.setObject(index++, game.getEndTime());
                } else {
                    ps.setTimestamp(index++, Timestamp.valueOf(game.getEndTime()));
                }
                if (winnerId == null) {
                    ps.setObject(index++, winnerId);
                } else {
                    ps.setLong(index++, winnerId);
                }
                try{
                    ps.setString(index++, TetrisField.toJson(game.getField1()));
                    if(game.getField2() == null){
                        ps.setObject(index++, null);
                    }else{
                        ps.setString(index++, TetrisField.toJson(game.getField2()));
                    }
                }catch(JsonProcessingException e){
                    throw new RuntimeException("Bad converting tetris field to json");
                }
                ps.setLong(index++, game.getPlayer1Time());
                if(game.getPlayer1Time() == null){
                    ps.setObject(index++,null);
                }else{
                    ps.setLong(index++, game.getPlayer2Time());
                }
                ps.setInt(index++, game.getPlayer1Points());
                ps.setInt(index++, game.getPlayer2Points());
                ps.setInt(index++, game.getDurationInMillis());
                return ps;
            }, keyHolder);
            return getOneById(keyHolder.getKey().longValue()).orElseThrow(() -> new RuntimeException("TetrisGame must exist in this context"));
        }
    }

    @Override
    public Optional<TetrisGame> getCurrentGameByPlayerId(Long playerId) {
        String condition = " WHERE (g.player1_id = ? OR g.player2_id = ?) AND end_time IS NULL";

        String query = this.getAllGamesWithFullPlayersInfo + condition;

        return jdbcTemplate.query(query, this.gameMapper, playerId, playerId)
                .stream().findAny();
    }

    @Override
    public Optional<TetrisGame> getOneById(Long gameId) {
        String condition = " WHERE g.id = ?";
        String query = this.getAllGamesWithFullPlayersInfo + condition;
        return jdbcTemplate.query(query, this.gameMapper, gameId)
                .stream()
                .findAny();
    }

    @Override
    public Set<TetrisGame> getAllByPlayerId(Long playerId) {
        String query = "SELECT * FROM tetris_game WHERE player1_id = ? OR player2_id = ?";
        return new HashSet<>(jdbcTemplate.query(query, this.gameMapper, playerId, playerId));
    }

    @Override
    public Set<TetrisGame> getAllGamesWithPlayersByPlayerId(Long playerId){
        String condition = "WHERE g.player1_id = ? OR g.player2_id = ?";
        String query = this.getAllGamesWithFullPlayersInfo+condition;
        return new HashSet<>(jdbcTemplate.query(query,this.gameMapper,playerId,playerId));
    }
}

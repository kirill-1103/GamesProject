package ru.krey.games.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.TttGame;
import ru.krey.games.utils.mapper.TttGameMapper;
import ru.krey.games.utils.mapper.TttGameWithPlayerMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TttGameJdbcTemplate implements TttGameDao {

    private final TttGameMapper gameMapper;
    private final JdbcTemplate jdbcTemplate;

    private final TttGameWithPlayerMapper tttGameWithPlayerMapper;
    private static final Logger log = LoggerFactory.getLogger(TttGameJdbcTemplate.class);

    private final String getAllGamesWithFullPlayersInfo =
            "SELECT  g.*,"+
            "p1.id AS p1_id, p1.last_game_code AS p1_last_game_code, p1.login AS p1_login, " +
                    "p1.password AS p1_password, p1.email as p1_email, p1.enabled as p1_enabled, " +
                    "p1.last_sign_in_time as p1_last_sign_in_time, p1.photo AS p1_photo, p1.rating AS p1_rating, " +
                    "p1.role AS p1_role, p1.sign_up_time AS p1_sign_up_time, " +
                    "p2.id AS p2_id, p2.last_game_code AS p2_last_game_code, p2.login AS p2_login, " +
                    "p2.password AS p2_password, p2.email as p2_email, p2.enabled as p2_enabled, " +
                    "p2.last_sign_in_time as p2_last_sign_in_time, p2.photo as p2_photo, p2.rating as p2_rating," +
                    "p2.role as p2_role, p2.sign_up_time as p2_sign_up_time " +
                    "FROM ttt_game AS g " +
                    "INNER JOIN player AS p1 ON g.player1_id = p1.id " +
                    "LEFT OUTER JOIN player AS p2 ON g.player2_id = p2.id ";

    @Override
    public Optional<TttGame> getOneById(Long id) {
        String query = "SELECT * FROM ttt_game WHERE id = ?";
        return jdbcTemplate.query(query, this.gameMapper, id)
                .stream()
                .findAny();
    }

    @Override
    public Set<TttGame> getAll() {
        String query = "SELECT * FROM ttt_game";
        return new HashSet<>(jdbcTemplate.query(query, this.gameMapper));
    }

    @Override
    public TttGame saveOrUpdate(TttGame game) {
        if (game == null) throw new IllegalArgumentException("TttGame object is null");

        final Long player2Id = game.getPlayer2() == null ? null : game.getPlayer2().getId();
        final Long winnerId = game.getWinner() == null ? null : game.getWinner().getId();

        if (game.getId() != null && getOneById(game.getId()).isPresent()) {
            /*update*/
//            log.info("Update TttGame: " + game);

            String query = "UPDATE ttt_game SET " +
                    "player1_id=?, player2_id=?, start_time=?,end_time=?," +
                    "winner_id=?, size_field=?, player1_time=?,player2_time=?,base_player_time=?," +
                    " actual_duration=?, victory_reason_code=?, complexity=?, queue=? WHERE id=? ";


            int rows = jdbcTemplate.update(query, game.getPlayer1().getId(), player2Id,
                    game.getStartTime(), game.getEndTime(), winnerId, game.getSizeField(),
                    game.getPlayer1Time(), game.getPlayer2Time(), game.getBasePlayerTime(),
                    game.getActualDuration(), game.getVictoryReasonCode(), game.getComplexity(),game.getQueue(), game.getId());
            if (rows != 1) {
                throw new RuntimeException("Invalid request to sql: " + query);
            }
            return game;
        } else {
            /*save*/
            log.info("Save TttGame: " + game);

            KeyHolder keyHolder = new GeneratedKeyHolder();

            String query = "INSERT INTO ttt_game (player1_id,player2_id,start_time," +
                    "end_time,winner_id,size_field,player1_time,player2_time,base_player_time,actual_duration," +
                    "victory_reason_code, complexity, queue)" +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id";

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
                if(game.getEndTime()==null){
                    ps.setObject(index++, game.getEndTime());
                }else{
                    ps.setTimestamp(index++, Timestamp.valueOf(game.getEndTime()));
                }
                if (winnerId == null) {
                    ps.setObject(index++, winnerId);
                } else {
                    ps.setLong(index++, winnerId);
                }
                ps.setInt(index++, game.getSizeField());
                ps.setLong(index++, game.getPlayer1Time());
                ps.setLong(index++, game.getPlayer2Time());
                ps.setLong(index++, game.getBasePlayerTime());
                ps.setInt(index++, game.getActualDuration());
                if(game.getVictoryReasonCode() == null){
                    ps.setObject(index++, null);
                }else{
                    ps.setInt(index++, game.getVictoryReasonCode());
                }
                if (game.getComplexity() == null) {
                    ps.setObject(index++, null);
                } else {
                    ps.setInt(index++, game.getComplexity());
                }
                ps.setByte(index++, game.getQueue());
                return ps;
            }, keyHolder);
            return getOneById(keyHolder.getKey().longValue()).orElseThrow(() -> new RuntimeException("TttGame must exist in this context"));
        }
    }

    @Override
    public Set<TttGame> getAllByPlayerId(Long playerId) {
        String query = "SELECT * FROM ttt_game WHERE player1_id = ? OR player2_id = ?";
        return new HashSet<>(jdbcTemplate.query(query, this.gameMapper, playerId, playerId));
    }

    @Override
    public Optional<TttGame> getCurrentGameByPlayerId(Long id) {
        String query = "SELECT * FROM ttt_game " +
                "WHERE (player1_id = ? OR player2_id = ?) AND end_time IS NULL";

        return jdbcTemplate.query(query, this.gameMapper, id, id)
                .stream().findAny();
    }

    @Override
    public Set<TttGame> getAllNoEnded(){
        String query = "SELECT * FROM ttt_game WHERE end_time is NULL";
        return new HashSet<>(jdbcTemplate.query(query,this.gameMapper));
    }

    @Override
    public Set<TttGame> getAllGamesWithPlayers(){
        return new HashSet<>(jdbcTemplate.query(this.getAllGamesWithFullPlayersInfo,this.tttGameWithPlayerMapper));
    }
    @Override
    public Set<TttGame> getAllGamesWithPlayersByPlayerId(Long playerId){
        String condition = "WHERE g.player1_id = ? OR g.player2_id = ?";
        String query = this.getAllGamesWithFullPlayersInfo+condition;
        return new HashSet<>(jdbcTemplate.query(query,this.tttGameWithPlayerMapper,playerId,playerId));
    }
}

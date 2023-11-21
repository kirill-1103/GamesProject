package ru.krey.games.playerservice.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.krey.games.playerservice.dao.interfaces.PlayerDao;
import ru.krey.games.playerservice.domain.Player;
import ru.krey.games.playerservice.utils.mapper.PlayerRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PlayerJdbcTemplate implements PlayerDao {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PlayerRowMapper playerRowMapper;

    @Override
    public Player saveOrUpdate(Player player) {
        if (player == null) throw new IllegalArgumentException("Player object is null");

        if (player.getId() != null && getOneById(player.getId()).isPresent()) {
            /*update*/
            log.info("Update player: " + player);

            String sql = "UPDATE player SET login=?, email=?, sign_up_time=?, last_sign_in_time=?," +
                    " rating=?, role=?, photo=?, enabled=?, password=?, last_game_code=? WHERE id = ?";
            int rows = jdbcTemplate.update(sql, player.getLogin(), player.getEmail(), player.getSignUpTime(), player.getLastSignInTime(),
                    player.getRating(), player.getRole(), player.getPhoto(), player.getEnabled(), player.getPassword(), player.getLastGameCode(), player.getId());
            if (rows != 1) {
                throw new RuntimeException("Invalid request to sql: " + sql);
            }
            return getOneById(player.getId()).orElseThrow(() -> new RuntimeException("Player must exist."));
        } else {
            /*save*/
            log.info("Save player: " + player);
            KeyHolder keyHolder = new GeneratedKeyHolder();


            String sql = "INSERT INTO player (login, password, email, sign_up_time, last_sign_in_time," +
                    "rating, role, photo, enabled,last_game_code) VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING id";

            jdbcTemplate.update(connection -> {
                int index = 1;

                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(index++, player.getLogin());
                ps.setString(index++, player.getPassword());
                ps.setString(index++, player.getEmail());
                ps.setTimestamp(index++, Timestamp.valueOf(player.getSignUpTime()));
                ps.setTimestamp(index++, Timestamp.valueOf(player.getLastSignInTime()));
                ps.setInt(index++, player.getRating());
                ps.setString(index++, player.getRole());
                ps.setString(index++, player.getPhoto());
                ps.setBoolean(index++, player.getEnabled());
                if (player.getLastGameCode() != null) {
                    ps.setInt(index++, player.getLastGameCode());
                } else {
                    ps.setObject(index++, null);
                }
                return ps;
            }, keyHolder);

            return getOneById(keyHolder.getKey().longValue())
                    .orElseThrow(() -> new RuntimeException("Player must exist in this context!"));
        }
    }

    @Override
    public Optional<Player> getOneById(Long id) {
        String query = "SELECT * FROM player WHERE id=? ";
        return jdbcTemplate.query(query, this.playerRowMapper, id)
                .stream()
                .findAny();
    }

    @Override
    public Set<Player> getAll() {
        String query = "SELECT * FROM player";
        return new HashSet<>(jdbcTemplate.query(query, this.playerRowMapper));
    }

    @Override
    public Optional<Player> getOneByLogin(String login) {
        String query = "SELECT * FROM player WHERE login=? ";
        return jdbcTemplate.query(query, this.playerRowMapper, login)
                .stream()
                .findAny();
    }

    @Override
    public Optional<Player> getOneByEmail(String email) {
        String query = "SELECT * FROM player WHERE email=?";
        return jdbcTemplate.query(query, this.playerRowMapper, email)
                .stream()
                .findAny();
    }

    @Override
    public List<Player> getAllOrderByRating() {
        String query = "SELECT * FROM PLAYER ORDER BY rating DESC";
        return jdbcTemplate.query(query, this.playerRowMapper);
    }

    @Override
    public Long getPlayerTopById(Long id) {
        String query = "SELECT row_number " +
                "FROM (" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY rating DESC) AS row_number, * " +
                "    FROM player" +
                ") AS player_with_row_numbers " +
                "WHERE player_with_row_numbers.id = ?";
        return jdbcTemplate.queryForObject(query, Long.class, id);
    }

    @Override
    public List<Player> getPlayersByPartOfName(String part) {
        String query = "SELECT * FROM player WHERE LOWER(player.login) LIKE CONCAT('%', ?,'%')";
        return new ArrayList<>(jdbcTemplate.query(query, this.playerRowMapper, part));
    }

    @Override
    public List<Player> getPlayersByPartOfEmail(String part) {
        String query = "SELECT * FROM player WHERE LOWER(player.email) LIKE CONCAT('%', ?,'%')";
        return new ArrayList<>(jdbcTemplate.query(query, this.playerRowMapper, part));
    }

    @Override
    public List<Player> getPlayersWithNameStarts(String part) {
        String query = "SELECT * FROM player WHERE LOWER(player.login) LIKE CONCAT('%', ?)";
        return new ArrayList<>(jdbcTemplate.query(query, this.playerRowMapper, part));
    }

    @Override
    public List<Player> getPlayersByLogins(List<String> names) {
        if (names.isEmpty()) {
            return Collections.emptyList();
        }
        String query = "SELECT * FROM player WHERE player.login in (:names)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("names", names);
        return new ArrayList<>(namedParameterJdbcTemplate.query(query, parameters, this.playerRowMapper));
    }

    @Override
    public void updateActive(Long id) {
        String sql = "UPDATE player SET active_time = NOW() WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateActive(String login) {
        String sql = "UPDATE player SET active_time = NOW() WHERE login = ?";
        jdbcTemplate.update(sql, login);
    }

    @Override
    public List<Player> getActivePlayersByTimeDiff(int seconds) {
        String query = "SELECT * FROM player WHERE EXTRACT(EPOCH FROM (NOW() - active_time)) < ?";
        return jdbcTemplate.query(query, this.playerRowMapper, seconds);
    }

    @Override
    public List<Player> getPlayersByLoginOrEmail(String login, String email) {
        String query = "SELECT * FROM player WHERE login = ? OR email = ?";
        return jdbcTemplate.query(query, this.playerRowMapper, login, email);
    }

    @Override
    public Set<Player> getPlayersByIds(List<Long> ids) {
        if(ids == null || ids.isEmpty()){
            return Set.of();
        }
        String query = "SELECT * FROM player WHERE player.id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("ids", ids);
        return new HashSet<>(namedParameterJdbcTemplate.query(query, parameters, this.playerRowMapper));
    }
}

package ru.krey.games.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.Player;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.mapper.PlayerMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PlayerJdbcTemplate implements PlayerDao {

    private static final Logger log = LoggerFactory.getLogger(PlayerJdbcTemplate.class);
    private final JdbcTemplate jdbcTemplate;
    private final PlayerMapper playerMapper;

    @Override
    public Player saveOrUpdate(Player player) {
        if (player == null) throw new IllegalArgumentException("Player object is null");

        if (player.getId() != null && getOneById(player.getId()).isPresent()) {
            /*update*/
            log.info("Update player: " + player);

            String sql = "UPDATE player SET login=?, email=?, sign_up_time=?, last_sign_in_time=?," +
                    " rating=?, role=?, photo=?, enabled=? WHERE id = ?";
            int rows = jdbcTemplate.update(sql, player.getLogin(), player.getEmail(), player.getSignUpTime(), player.getLastSignInTime(),
                    player.getRating(), player.getRole(), player.getPhoto(), player.getEnabled(), player.getId());
            if (rows != 1) {
                throw new RuntimeException("Invalid request to sql: " + sql);
            }
            return player;
        } else {
            /*save*/
            log.info("Save player: " + player);
            KeyHolder keyHolder = new GeneratedKeyHolder();


            String sql = "INSERT INTO player (login, password, email, sign_up_time, last_sign_in_time," +
                    "rating, role, photo, enabled) VALUES (?,?,?,?,?,?,?,?,?) RETURNING id";

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
                ps.setBoolean(index, player.getEnabled());
                return ps;
            }, keyHolder);

            return getOneById(keyHolder.getKey().longValue())
                    .orElseThrow(() -> new RuntimeException("Player must exist in this context!"));
        }
    }

    @Override
    public Optional<Player> getOneById(Long id) {
        String query = "SELECT * FROM player WHERE id=? ";
        return jdbcTemplate.query(query, this.playerMapper, id)
                .stream()
                .findAny();
    }

    @Override
    public Set<Player> getAll() {
        String query = "SELECT * FROM player";
        return new HashSet<>(jdbcTemplate.query(query, this.playerMapper));
    }

    @Override
    public Optional<Player> getOneByLogin(String login) {
        String query = "SELECT * FROM player WHERE login=? ";
        return jdbcTemplate.query(query, this.playerMapper, login)
                .stream()
                .findAny();
    }

    @Override
    public Optional<Player> getOneByEmail(String email) {
        String query = "SELECT * FROM player WHERE email=?";
        return jdbcTemplate.query(query, this.playerMapper, email)
                .stream()
                .findAny();
    }
}

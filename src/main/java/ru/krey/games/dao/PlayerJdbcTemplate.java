package ru.krey.games.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.Player;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.mapper.PlayerMapper;

import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PlayerJdbcTemplate implements PlayerDao {

    private final JdbcTemplate jdbcTemplate;
    private final PlayerMapper playerMapper;

    @Override
    public void saveOrUpdate(Player player) {
        if (player.getId() != null && getOneById(player.getId()) != null) {
            /*update*/
            String sql = "UPDATE player SET login=?, email=?, sign_up_time=?, last_sign_in_time=?," +
                    " rating=?, role=?, photo=?, enabled=? WHERE id = ?";
            jdbcTemplate.update(sql, player.getLogin(), player.getEmail(), player.getSignUpTime(), player.getLastSignInTime(),
                    player.getRating(), player.getRole(), player.getPhoto(), player.getEnabled(), player.getId());
        } else {
            /*save*/
            String sql = "INSERT INTO player (login, password, email, sign_up_time, last_sign_in_time," +
                    "rating, role, photo, enabled) VALUES (?,?,?,?,?,?,?,?,?)";
            jdbcTemplate.update(sql, player.getLogin(), player.getPassword(), player.getEmail(), player.getSignUpTime(), player.getLastSignInTime(),
                    player.getRating(), player.getRole(), player.getPhoto(), player.getEnabled());
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
        return null;
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
        return jdbcTemplate.query(query, this.playerMapper,email)
                .stream()
                .findAny();
    }
}

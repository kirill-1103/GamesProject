package ru.krey.games.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.domain.TttGame;
import ru.krey.games.mapper.TttGameMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TttGameJdbcTemplate implements TttGameDao {

    private final TttGameMapper gameMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<TttGame> getOneById(Long id) {
        String query = "SELECT * FROM ttt_game WHERE id = ?";
        return jdbcTemplate.query(query,this.gameMapper,id)
                .stream()
                .findAny();
    }
}

package ru.krey.games.utils.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.domain.Player;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.PlayerService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class GameMessageMapper implements RowMapper<GameMessage> {
    private final PlayerService playerService;

    @Override
    public GameMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GameMessage.builder()
                .id(rs.getLong("id"))
                .gameId(rs.getLong("game_id"))
                .gameCode(rs.getInt("game_code"))
                .sender(getPlayerById(rs.getLong("sender_id")))
                .time(rs.getTimestamp("time").toLocalDateTime())
                .message(rs.getString("message"))
                .build();
    }

    private Player getPlayerById(Long id){
        Player player = playerService.getOneById(id);
        if(Objects.isNull(player)){
            throw new NotFoundException("Player must exist in this context");
        }
        return player;
    }

}

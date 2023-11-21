package ru.krey.games.utils.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.games.tetris.TetrisGame;
import ru.krey.games.domain.games.ttt.TttGame;
import ru.krey.games.logic.tetris.TetrisField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class TetrisGameMapper implements RowMapper<TetrisGame> {
    @Override
    public TetrisGame mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDateTime endTime = rs.getTimestamp("end_time") == null ? null : rs.getTimestamp("end_time").toLocalDateTime();

//        Player player1 = MapperUtils.getPlayerFromRsByPrefix(rs, "p1_");
//        Player player2 = MapperUtils.getPlayerFromRsByPrefix(rs, "p2_");
//        Player winner;
//        if(rs.getObject("winner_id") == null){
//            winner = null;
//        }else if(rs.getLong("winner_id") == player1.getId()){
//            winner = player1;
//        }else{
//            winner = player2;
//        }
        try {
            return TetrisGame.builder()
                    .id(rs.getLong("id"))
                    .player1Id(rs.getLong("player1_id"))
                    .player2Id(rs.getLong("player2_id"))
//                    .player1(player1)
//                    .player2(player2)
                    .startTime(rs.getTimestamp("start_time").toLocalDateTime())
                    .endTime(endTime)
//                    .winner(winner)
                    .player1Time(rs.getLong("player1_time"))
                    .player2Time(rs.getLong("player2_time"))
                    .duration(rs.getInt("duration"))
                    .player1Points(rs.getInt("player1_points"))
                    .player2Points(rs.getInt("player2_points"))
                    .field1(TetrisField.fromJson(rs.getString("field_1")))
                    .field2(TetrisField.fromJson(rs.getString("field_2")))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Bad converting from json to tetris field");
        }
    }
}

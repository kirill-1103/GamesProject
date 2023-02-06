package ru.krey.games.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TttMoveDto {
    private Long id;

    @JsonAlias("absolute_time")
    private LocalDateTime absoluteTime;

    @JsonAlias("game_time_millis")
    private Integer gameTimeMillis;

    @JsonAlias("x_coord")
    private Integer xCoord;
    
    @JsonAlias("y_coord")
    private Integer yCoord;

    @JsonAlias("player_id")
    private Long playerId;

    @JsonAlias("game_id")
    private Long gameId;

}

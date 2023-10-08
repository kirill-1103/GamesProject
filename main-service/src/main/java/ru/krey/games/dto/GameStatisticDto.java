package ru.krey.games.dto;

import lombok.Builder;
import lombok.Data;
import ru.krey.games.domain.interfaces.Game;

import java.time.LocalDateTime;

@Data
@Builder
public class GameStatisticDto {
    private Long id;
    private String name;
    private Integer code;
    private LocalDateTime time;
    private String entityName;
    private String result;

    public static GameStatisticDto byInfo(Game game, String entityName, String gameResult){
        return GameStatisticDto.builder()
                .name(game.getRussianName())
                .time(game.getStartTime())
                .code(game.getGameCode())
                .entityName(entityName)
                .id(game.getId())
                .result(gameResult)
                .build();
    }
}

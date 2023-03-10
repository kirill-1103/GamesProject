package ru.krey.games.dto;

import lombok.Builder;
import lombok.Data;

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
}

package ru.krey.games.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TttGameDto {
    private Long id;

    @JsonAlias("player1_id")
    private Long player1Id;

    @JsonAlias("player2_id")
    private Long player2Id;

    @JsonAlias("start_time")
    private LocalDateTime startTime;

    @JsonAlias("end_time")
    private LocalDateTime endTime;

    @JsonAlias("winner_id")
    private Long winnerId;

    @JsonAlias("size_field")
    private Integer sizeField;

    @JsonAlias("player1_time")
    private Long player1Time;

    @JsonAlias("player2_time")
    private Long player2Time;

    @JsonAlias("base_player_time")
    private Long basePlayerTime;

    @JsonAlias("actual_duration")
    private Integer actualDuration;

    private Integer complexity;

    @JsonAlias("victory_reason_code")
    private Byte victoryReasonCode;

    private Byte queue;
}

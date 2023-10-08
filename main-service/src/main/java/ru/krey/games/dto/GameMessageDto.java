package ru.krey.games.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameMessageDto {
    private Long id;

    @JsonAlias("game_id")
    private Long gameId;

    @JsonAlias("game_code")
    private Integer gameCode;

    @JsonAlias("sender_id")
    private Long senderId;

    private LocalDateTime time;

    private String message;

}

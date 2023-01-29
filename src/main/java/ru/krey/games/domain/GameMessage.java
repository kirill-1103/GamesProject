package ru.krey.games.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class GameMessage {
    private Long id;
    private int gameId;
    private int gameCode;
    private Player sender;
    private LocalDateTime time;
    private String message;
}

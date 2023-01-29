package ru.krey.games.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class GameMessage {

    @NonNull
    private Long id;

    @NonNull
    private Long gameId;

    @NonNull
    private Integer gameCode;

    private Player sender;

    @NonNull
    private LocalDateTime time;

    @NonNull
    private String message;
}

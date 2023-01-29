package ru.krey.games.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class TttMove {
    @NonNull
    private Long id;

    @NonNull
    private LocalDateTime absoluteTime;

    @NonNull
    private Integer gameTimeMillis;

    @NonNull
    private Integer xCoordinate;

    @NonNull
    private Integer yCoordinate;

    private Player player;

    @NonNull
    private TttGame game;
}

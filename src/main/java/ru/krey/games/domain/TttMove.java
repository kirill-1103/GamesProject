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
    private Long id;
    private LocalDateTime absolute_time;
    private Integer game_time_millis;
    private int x_coordinate;
    private int y_coordinate;
    private Player player;
    private TttGame game;
}

package ru.krey.games.domain.games.ttt;

import lombok.*;
import ru.krey.games.domain.Player;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class TttMove {
    private Long id;

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

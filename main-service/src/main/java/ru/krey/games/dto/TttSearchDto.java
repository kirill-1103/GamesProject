package ru.krey.games.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TttSearchDto{
    @JsonAlias("player_id") private Long playerId;
    @JsonAlias("size_field") private Integer sizeField;
    @JsonAlias("base_player_time") private Long basePlayerTime;
}


package ru.krey.games.dto.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.krey.games.domain.TttGame;
import ru.krey.games.dto.TttGameDto;

@RequiredArgsConstructor
@Component
public class TttGameToDtoConverter implements Converter<TttGame, TttGameDto> {

    @Override
    public TttGameDto convert(TttGame game) {
        Long player2Id = game.getPlayer2() == null ? null : game.getPlayer2().getId();
        Long winnerId = game.getWinner() == null ? null : game.getWinner().getId();

        return TttGameDto.builder()
                .id(game.getId())
                .basePlayerTime(game.getBasePlayerTime())
                .actualDuration(game.getActualDuration())
                .endTime(game.getEndTime())
                .complexity(game.getComplexity())
                .player1Time(game.getPlayer1Time())
                .player2Time(game.getPlayer2Time())
                .sizeField(game.getSizeField())
                .startTime(game.getStartTime())
                .player1Id(game.getPlayer1().getId())
                .player2Id(player2Id)
                .victoryReasonCode(game.getVictoryReasonCode())
                .winnerId(winnerId)
                .queue(game.getQueue())
                .field(game.getField())
                .gameCode(game.getGameCode())
                .build();
    }
}

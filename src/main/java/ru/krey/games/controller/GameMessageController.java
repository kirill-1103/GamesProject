package ru.krey.games.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.GameMessageDao;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.dto.GameMessageDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.service.GameMessageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/game_message")
@RequiredArgsConstructor
@Slf4j
public class GameMessageController {
    private final ConversionService conversionService;

    private final SimpMessagingTemplate messagingTemplate;

    private final GameMessageService gameMessageService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @GetMapping("/{game_id}/{game_code}")
    public List<GameMessageDto> getGameMessages(@PathVariable("game_id") @NonNull Long gameId,
                                                @PathVariable("game_code") @NonNull Integer gameCode){
        return gameMessageService.getMessagesByGameIdAndCode(gameId,gameCode)
                .stream()
                .map(gameMessage->conversionService.convert(gameMessage,GameMessageDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/new")
    public void saveMessage(@RequestBody GameMessageDto messageDto){
        if(Objects.isNull(messageDto.getSenderId()) || Objects.isNull(messageDto.getMessage())
        || Objects.isNull(messageDto.getGameId()) || Objects.isNull(messageDto.getGameCode())
        || messageDto.getMessage().isBlank()){
            throw new RuntimeException("Wrong GameMessage");
        }
        messageDto.setTime(LocalDateTime.now());

        executor.execute(()->{
            gameMessageService.saveOrUpdate(conversionService.convert(messageDto, GameMessage.class));
        });

        messagingTemplate.convertAndSend(
                "/topic/game_message/"+messageDto.getGameId()+"/"+messageDto.getGameCode(),
                messageDto);

    }
}

package ru.krey.games.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.domain.Message;
import ru.krey.games.domain.Player;
import ru.krey.games.dto.DialogDto;
import ru.krey.games.dto.MessageDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.service.DialogService;
import ru.krey.games.service.MessageService;
import ru.krey.games.service.PlayerService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
@Slf4j
public class MessageController {

    private final PlayerService playerService;
    private final ConversionService conversionService;

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageService messageService;

    private final DialogService dialogService;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class PlayerDto{
        String login;
        Long id;
    }

    private List<PlayerDto> onlineList = new ArrayList<>();

    @GetMapping("/info")
    public List<DialogDto> getDialogsInfo(@RequestParam("player_id") Long playerId) {
        List<Message> messages = messageService.getLastMessagesByPlayerId(playerId);
        return dialogService.convertLastMessagesToDialogs(messages, playerId);
    }

    @GetMapping("/dialog")
    public ResponseEntity<?> getDialog(@RequestParam("player1_id") Long player1Id, @RequestParam("player2_id") Long player2Id, Principal principal) {
        Player player1 = playerService.getOneById(player1Id);
        Player player2 = playerService.getOneById(player2Id);
        if(!player1.getLogin().equals(principal.getName()) && !player2.getLogin().equals(principal.getName())){
            throw new BadRequestException("Нет прав доступа к сообщениям");
        }
        return ResponseEntity.ok(dialogService.getDialog(player1,player2));
    }

    @GetMapping("")
    public List<DialogDto> getAllDialogsByPlayer(@RequestParam("player_id") Long playerId, Principal principal) {
        Player player = playerService.getOneById(playerId);

        if(!player.getLogin().equals(principal.getName())){
            throw new BadRequestException("Нет прав доступа к сообщениям");
        }

        return dialogService.getAllDialogsByPlayer(player);
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageDto message){
        if(Objects.isNull(message) || message.getMessageText().isBlank()
                || Objects.isNull(message.getSenderId()) || Objects.isNull(message.getRecipientId())){
            throw new BadRequestException("Wrong message:"+message);
        }
        message.setSendingTime(LocalDateTime.now());
        Message savedMessage = messageService.saveOrUpdate(conversionService.convert(message, Message.class));
        message.setId(savedMessage.getId());
        sendSocketMessage(message);
    }
    @PostMapping("/set_reading_time")
    public void setReadingTime(@RequestBody List<MessageDto> messages){
        messageService.updateReadingTime(messages.stream().map(MessageDto::getId).collect(Collectors.toList()));
    }

    @GetMapping("/online")
    public List<Long> getOnlinePlayersIds(){
        return this.onlineList.stream().map(PlayerDto::getId).collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 5000)
    public void updateOnlineUsers(){
        List<Player> onlinePlayers = playerService.getActivePlayers();
        this.onlineList = onlinePlayers.stream().map(pl-> new PlayerDto(pl.getLogin(),pl.getId())).collect(Collectors.toList());;
    }


    private void sendSocketMessage(MessageDto message){
        long senderId = message.getSenderId();
        long recipientId = message.getRecipientId();

        messagingTemplate.convertAndSend("/topic/chat/"+senderId,message);
        messagingTemplate.convertAndSend("/topic/chat/"+recipientId,message);
    }
}

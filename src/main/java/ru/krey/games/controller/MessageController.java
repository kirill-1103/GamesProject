package ru.krey.games.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.krey.games.dao.interfaces.MessageDao;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Message;
import ru.krey.games.domain.Player;
import ru.krey.games.dto.DialogDto;
import ru.krey.games.dto.MessageDto;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.PlayerService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class MessageController {
    private final MessageDao messageDao;

    private final PlayerService playerService;

    private final PlayerDao playerDao;

    private final ConversionService conversionService;

    private final SimpMessagingTemplate messagingTemplate;

    private final SessionRegistry sessionRegistry;

    private final static Logger log = LoggerFactory.getLogger(MessageController.class);

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
        List<Message> messages = messageDao.getLastMessagesByPlayerId(playerId);
        List<DialogDto> dialogs = new ArrayList<>();
        messages.forEach(message -> {
            Player tempPlayer;
            Player otherPlayer;
            if (message.getRecipient().getId().equals(playerId)) {
                tempPlayer = message.getRecipient();
                otherPlayer = message.getSender();
            } else {
                tempPlayer = message.getSender();
                otherPlayer = message.getRecipient();
            }
            DialogDto dialogDto = new DialogDto();
            dialogDto.setPlayer2(otherPlayer);
            dialogDto.setPlayer1(tempPlayer);
            dialogDto.addMessage(message);
            dialogDto.setLastMessage(message);
            dialogs.add(dialogDto);
        });
        return dialogs;
    }

    @GetMapping("/dialog")
    public ResponseEntity<?> getDialog(@RequestParam("player1_id") Long player1Id, @RequestParam("player2_id") Long player2Id, Principal principal) {
        Player player1 = playerDao.getOneById(player1Id).orElseThrow(()->{throw new NotFoundException("игрок не найден");});
        Player player2 = playerDao.getOneById(player2Id).orElseThrow(()->{throw new NotFoundException("игрок не найден");});

        if(!player1.getLogin().equals(principal.getName()) && !player2.getLogin().equals(principal.getName())){
            throw new BadRequestException("Нет прав доступа к сообщениям");
        }
        List<Message> messages = messageDao.getAllMessagesBetweenPlayers(player1Id, player2Id);
        DialogDto dialogDto = new DialogDto();
        dialogDto.setMessagesByList(messages);
        if (messages.isEmpty()) {
            dialogDto.setPlayer1(playerDao.getOneById(player1Id).orElseThrow(() -> new BadRequestException("Player with id " + player1Id + " not exists")));
            dialogDto.setPlayer2(playerDao.getOneById(player2Id).orElseThrow(() -> new BadRequestException("Player with id " + player2Id + " not exists")));
            dialogDto.setMessagesByList(Collections.emptyList());
        } else {
            dialogDto.setPlayer1(messages.get(0).getSender());
            dialogDto.setPlayer2(messages.get(0).getRecipient());
        }
        return ResponseEntity.ok(dialogDto);
    }

    @GetMapping("")
    public List<DialogDto> getAllDialogsByPlayer(@RequestParam("player_id") Long playerId, Principal principal) {
        Player player = playerDao.getOneById(playerId).orElseThrow(()->{throw new NotFoundException("игрок не найден");});

        if(!player.getLogin().equals(principal.getName())){
            throw new BadRequestException("Нет прав доступа к сообщениям");
        }
        List<Message> messages = messageDao.getAllMessagesByPlayerId(playerId);
        HashMap<Player, DialogDto> dialogs = new HashMap<>();
        messages.forEach(message -> {
            Player tempPlayer;
            Player otherPlayer;
            if (message.getRecipient().getId().equals(playerId)) {
                tempPlayer = message.getRecipient();
                otherPlayer = message.getSender();
            } else {
                tempPlayer = message.getSender();
                otherPlayer = message.getRecipient();
            }
            if (!dialogs.containsKey(otherPlayer)) {
                DialogDto dialog = new DialogDto();
                dialog.setPlayer1(tempPlayer);
                dialog.setPlayer2(otherPlayer);
                dialogs.put(otherPlayer, dialog);
            }
            dialogs.get(otherPlayer).addMessage(message);
        });
        return new ArrayList<>(dialogs.values());
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MessageDto message){
        if(Objects.isNull(message) || message.getMessageText().isBlank()
                || Objects.isNull(message.getSenderId()) || Objects.isNull(message.getRecipientId())){
            throw new BadRequestException("Wrong message:"+message);
        }
        message.setSendingTime(LocalDateTime.now());
        Message savedMessage = messageDao.saveOrUpdate(conversionService.convert(message, Message.class));
        message.setId(savedMessage.getId());
        sendSocketMessage(message);
    }
    @PostMapping("/set_reading_time")
    public void setReadingTime(@RequestBody List<MessageDto> messages){
        messageDao.updateReadingTime(messages.stream().map(MessageDto::getId).collect(Collectors.toList()));
    }

    @GetMapping("/online")
    public List<Long> getOnlinePlayersIds(){
        return this.onlineList.stream().map(PlayerDto::getId).collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 5000)
    public void updateOnlineUsers(){
        List<String> userNames = sessionRegistry.getAllPrincipals().stream().map(pr -> ((UserDetails) pr).getUsername()).collect(Collectors.toList());
        List<PlayerDto> newOnlines = new ArrayList<>();
        for(String name: userNames){
            if(!this.onlineList.stream().map(PlayerDto::getLogin).collect(Collectors.toList()).contains(name)){
                PlayerDto playerDto = new PlayerDto();
                playerDto.login = name;
                newOnlines.add(playerDto);
            }else{
                PlayerDto player = this.onlineList.stream().filter(p->p.login.equals(name)).findAny().get();
                newOnlines.add(player);
            }
        }
        List<String> names = new ArrayList<>();
        for(PlayerDto playerDto : newOnlines){
            if(Objects.isNull(playerDto.id)){
                names.add(playerDto.login);
            }
        }

        List<Player> newPlayers = playerDao.getPlayersByLogins(names);

        for(Player player: newPlayers){
           PlayerDto playerDto  = newOnlines.stream().filter(p->p.getLogin().equals(player.getLogin())).findAny().get();
           playerDto.id = player.getId();
        }


        this.onlineList = newOnlines;
    }


    private void sendSocketMessage(MessageDto message){
        long senderId = message.getSenderId();
        long recipientId = message.getRecipientId();

        messagingTemplate.convertAndSend("/topic/chat/"+senderId,message);
        messagingTemplate.convertAndSend("/topic/chat/"+recipientId,message);
    }



}

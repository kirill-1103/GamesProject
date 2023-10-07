package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krey.games.domain.Message;
import ru.krey.games.domain.Player;
import ru.krey.games.dto.DialogDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DialogService {

    private final MessageService messageService;

    public DialogDto getDialog(Player player1, Player player2){
        List<Message> messages = messageService.getAllMessagesBetweenPlayers(player1.getId(), player2.getId());
        DialogDto dialogDto = new DialogDto();
        dialogDto.setMessagesByList(messages);
        if (messages.isEmpty()) {
            dialogDto.setPlayer1(player1);
            dialogDto.setPlayer2(player2);
            dialogDto.setMessagesByList(Collections.emptyList());
        } else {
            dialogDto.setPlayer1(messages.get(0).getSender());
            dialogDto.setPlayer2(messages.get(0).getRecipient());
        }
        return dialogDto;
    }

    public List<DialogDto> getAllDialogsByPlayer(Player player){
        List<Message> messages = messageService.getAllMessagesByPlayerId(player.getId());
        HashMap<Player, DialogDto> dialogs = new HashMap<>();
        messages.forEach(message -> {
            Player tempPlayer;
            Player otherPlayer;
            if (message.getRecipient().getId().equals(player.getId())) {
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

    public List<DialogDto> convertLastMessagesToDialogs(List<Message> messages, Long playerId){
        return messages.stream().map((message)->{
            Player tempPlayer;
            Player otherPlayer;
            if (message.getRecipient().getId().equals(playerId)) {
                tempPlayer = message.getRecipient();
                otherPlayer = message.getSender();
            } else {
                tempPlayer = message.getSender();
                otherPlayer = message.getRecipient();
            }
            return DialogDto.builder()
                    .player1(tempPlayer)
                    .player2(otherPlayer)
                    .lastMessage(message)
                    .messages(new PriorityQueue<Message>(){{add(message);}})
                    .build();
        }).collect(Collectors.toList());
    }
}

package ru.krey.games.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.krey.games.domain.Message;
import ru.krey.games.domain.Player;
import ru.krey.games.error.BadRequestException;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DialogDto {
    private PriorityQueue<Message> messages = new PriorityQueue<>(Comparator.comparing(Message::getSendingTime));

    private Player player1;

    private Player player2;

    private Message lastMessage;

    public void addMessage(Message message){
        if(!message.getRecipient().getId().equals(player1.getId()) && !message.getSender().getId().equals(player1.getId())){
            throw new IllegalArgumentException("Неподходящий диалог");
        }
        if(!message.getRecipient().getId().equals(player2.getId()) && !message.getSender().getId().equals(player2.getId())){
            throw new IllegalArgumentException("Неподходящий диалог");
        }
        messages.add(message);
        for(int i = 0;i<100;i++){
            System.out.println("hello world");
        }
    }

    public void setMessagesByList(List<Message> messages){
        if(!messages.isEmpty()){
            this.messages.addAll(messages);
            this.lastMessage = this.messages.peek();
        }
    }
}

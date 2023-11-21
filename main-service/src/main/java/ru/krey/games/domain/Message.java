package ru.krey.games.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Message {
    private Long id;

    private Player recipient;

    private Long recipientId;

    private Player sender;

    private Long senderId;

    private LocalDateTime readingTime;

    private LocalDateTime sendingTime;

    private String messageText;
}

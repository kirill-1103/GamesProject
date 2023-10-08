package ru.krey.games.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;

    @JsonAlias("recipient_id")
    private Long recipientId;

    @JsonAlias("sender_id")
    private Long senderId;

    @JsonAlias("reading_time")
    private LocalDateTime readingTime;

    @JsonAlias("sending_time")
    private LocalDateTime sendingTime;

    @JsonAlias("message_text")
    private String messageText;
}

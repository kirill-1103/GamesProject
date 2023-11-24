package ru.krey.games.playerservice.domain;

import lombok.*;
import ru.krey.games.playerservice.openapi.model.FriendResponseEnumOpenApi;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequest {
    private Long id;

    @NonNull
    private Long senderId;

    @NonNull
    private Long receiverId;

    private FriendResponseEnumOpenApi response;

    private LocalDateTime requestDate;

    private LocalDateTime responseDate;
}

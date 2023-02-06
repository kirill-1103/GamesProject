package ru.krey.games.domain;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Player {
    private Long id;

    @NonNull
    private String login;

    @NonNull
    private String email;

    private String password;

    @NonNull
    private LocalDateTime signUpTime;

    @NonNull
    private LocalDateTime lastSignInTime;

    @NonNull
    private Integer rating;

    private String photo;

    private Boolean enabled;

    @NonNull
    private String Role;

    private Integer lastGameCode;
}

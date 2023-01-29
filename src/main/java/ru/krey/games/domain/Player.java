package ru.krey.games.domain;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
public class Player {
    @NonNull
    private Long id;

    @NonNull
    private String login;

    @NonNull
    private String email;

    @NonNull
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
}
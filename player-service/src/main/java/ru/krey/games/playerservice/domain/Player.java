package ru.krey.games.playerservice.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Player {
    private final static int CHANGE_RATING = 30;

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
    private String role;

    private Integer lastGameCode;

    public void minusRating() {
        if (this.rating < CHANGE_RATING) {
            this.rating = 0;
        } else {
            this.rating -= CHANGE_RATING;
        }
    }

    public void plusRating() {
        this.rating += CHANGE_RATING;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.of(this.getRole()).map(SimpleGrantedAuthority::new).stream().collect(Collectors.toList());
    }
}

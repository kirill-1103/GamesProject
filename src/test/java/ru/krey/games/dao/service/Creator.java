package ru.krey.games.dao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.krey.games.dao.interfaces.GameMessageDao;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.domain.Player;
import ru.krey.games.service.RoleService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class Creator {
    private final PlayerDao playerDao;
    private final GameMessageDao gameMessageDao;

     public Player createPlayer(String login, String email){
        return playerDao.saveOrUpdate(Player.builder()
                .email(email)
                .login(login)
                .lastSignInTime(LocalDateTime.now())
                .signUpTime(LocalDateTime.now())
                .password(new BCryptPasswordEncoder().encode("123"))
                .photo(null)
                .Role(RoleService.ROLE_USER)
                .rating(0)
                .enabled(true)
                .build());
    }

    public GameMessage createMessage(String message){
        return gameMessageDao.saveOrUpdate(GameMessage.builder()
                .message(message)
                .gameId(2L)
                .gameCode(2)
                .sender(createPlayer("player_with_message_"+message,"email"+message+"@mail.com"))
                .time(LocalDateTime.now())
                .build()
        );
     }
}

package ru.krey.games.dao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.krey.games.dao.interfaces.GameMessageDao;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.domain.*;
import ru.krey.games.domain.games.ttt.TttGame;
import ru.krey.games.domain.games.ttt.TttMove;
import ru.krey.games.utils.RoleUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Creator {
    private final PlayerDao playerDao;
    private final GameMessageDao gameMessageDao;

    private final TttGameDao gameDao;

    private final TttMoveDao moveDao;

    public Player createPlayerSave(String login, String email){
        Optional<Player> player = playerDao.getOneByLogin(login);
        return player.orElseGet(()->createPlayer(login,email));
    }
     public Player createPlayer(String login, String email){
         return playerDao.saveOrUpdate(Player.builder()
                 .email(email)
                 .login(login)
                 .lastSignInTime(LocalDateTime.now())
                 .signUpTime(LocalDateTime.now())
                 .password(new BCryptPasswordEncoder().encode("123"))
                 .photo(null)
                 .Role(RoleUtils.ROLE_USER)
                 .rating(0)
                 .enabled(true)
                 .build());
     }

    public GameMessage createGameMessage(String message){
        return gameMessageDao.saveOrUpdate(GameMessage.builder()
                .message(message)
                .gameId(2L)
                .gameCode(2)
                .sender(getPlayerByLogin("player_with_message_"+message,"email"+message+"@mail.com"))
                .time(LocalDateTime.now())
                .build()
        );
     }

     public TttGame createGame(String login1, String email1, String login2, String email2, int sizeField){
         Player player1 = getPlayerByLogin(login1,email1);
         Player player2 = login2 == null ? null : createPlayer(login2,email2);
         return gameDao.saveOrUpdate(TttGame.builder()
                 .player1(player1)
                 .player2(player2)
                 .player1Time(100L)
                 .player2Time(100L)
                 .basePlayerTime(100L)
                 .actualDuration(100)
                 .endTime(LocalDateTime.now())
                 .startTime(LocalDateTime.now().minusSeconds(1))
                 .sizeField(sizeField)
                 .victoryReasonCode((byte) 3)
                 .winner(player1)
                 .build());
     }

     public TttMove createMove(Integer x, Integer y){
         Player player = playerDao.getOneByLogin("login_for_move_1").
                 orElse(createPlayerSave("login_for_move_1","email_for_move_1@mail.com"));
         return moveDao.saveOrUpdate(TttMove.builder()
                         .game(createGame(player.getLogin(),player.getEmail(),null,null,3))
                         .player(playerDao.getOneByLogin("login_for_move_1").get())
                         .xCoordinate(x)
                         .yCoordinate(y)
                         .gameTimeMillis(200)
                         .absoluteTime(LocalDateTime.now())
                         .build());
     }

     public TttMove createMoveWithoutPlayer(Integer x, Integer y){
         Player player = getPlayerByLogin("login_for_move_1","login_for_email_1@mail.com");
         return moveDao.saveOrUpdate(TttMove.builder()
                 .game(createGame(player.getLogin(),player.getEmail(),null,null,3))
                 .player(null)
                 .xCoordinate(x)
                 .yCoordinate(y)
                 .gameTimeMillis(200)
                 .absoluteTime(LocalDateTime.now())
                 .build());
     }

     private Player getPlayerByLogin(String login,String email){
         return playerDao.getOneByLogin(login).
                 orElse(createPlayerSave(login,email));
    }

    public Message createMessage(String text, Player sender, Player recipient){
        return Message.builder()
                .messageText(text)
                .sender(sender)
                .recipient(recipient)
                .sendingTime(LocalDateTime.now())
                .build();
    }
}

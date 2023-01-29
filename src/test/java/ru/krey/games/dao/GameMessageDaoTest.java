package ru.krey.games.dao;

import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.krey.games.dao.interfaces.GameMessageDao;
import ru.krey.games.dao.service.Creator;
import ru.krey.games.dao.service.TimeComparator;
import ru.krey.games.domain.GameMessage;
import ru.krey.games.domain.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
@NoArgsConstructor
@TestPropertySource("/application-test.properties")
public class GameMessageDaoTest {
    @Autowired
    private GameMessageDao gameMessageDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Creator creator;

    @Autowired
    private TimeComparator timeComparator;

    private List<String> messages;
    private List<Long> ids;

    @Before
    public void fill(){
        jdbcTemplate.update("DELETE FROM game_message");
        jdbcTemplate.update("DELETE FROM player");
        messages = new ArrayList<>();
        ids = new ArrayList<>();
        for(int i = 0;i<10;i++){
            messages.add("Message"+i);
        }
        messages.forEach(message->{
            ids.add(creator.createMessage(message).getId());
        });
    }

    @After
    public void remove(){
        jdbcTemplate.update("DELETE FROM player");
        jdbcTemplate.update("DELETE FROM game_message");
    }



    @Test
    public void testAutowired(){
        Assertions.assertThat(gameMessageDao).isNotNull();
    }

    @Test
    public void testGetOne(){
        this.ids.forEach(id->{
            GameMessage gameMessage = gameMessageDao.getOneById(id).orElse(null);
            Assertions.assertThat(gameMessage).isNotNull();
            Assertions.assertThat(gameMessage.getMessage()).isEqualTo(this.messages.get(ids.indexOf(id)));
        });
    }

    @Test
    public void testSuccessSave(){
        GameMessage gameMessage = creator.createMessage("Some message");

        Assertions.assertThat(gameMessage).isNotNull();
        Assertions.assertThat(gameMessage.getId()).isNotNull();

        GameMessage gameMessageFromDb = gameMessageDao.getOneById(gameMessage.getId()).orElse(null);
        Assertions.assertThat(gameMessageFromDb).isNotNull();
        Assertions.assertThat(gameMessageFromDb).isEqualTo(gameMessage);
    }

    @Test
    public void testFailedSave(){
        Throwable thrown1 = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,()-> {
                    gameMessageDao.saveOrUpdate(null);
                }
        );
        Assertions.assertThat(thrown1).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testUpdate(){
        GameMessage message = gameMessageDao.getOneById(ids.get(0)).orElse(null);
        Assertions.assertThat(message).isNotNull();

        message.setMessage("my_message");
        message.setGameId(134L);
        message.setGameCode(123);
        message.setSender(creator.createPlayer("Some player","someemail@mail.com"));
        gameMessageDao.saveOrUpdate(message);

        GameMessage messageAfterUpdate = gameMessageDao.getOneById(ids.get(0)).orElse(null);
        Assertions.assertThat(messageAfterUpdate).isNotNull();
        Assertions.assertThat(messageAfterUpdate).isEqualTo(message);

        //check time
        message.setTime(LocalDateTime.now());
        gameMessageDao.saveOrUpdate(message);

        messageAfterUpdate = gameMessageDao.getOneById(ids.get(0)).orElse(null);
        LocalDateTime date1 = message.getTime();
        LocalDateTime date2 = messageAfterUpdate.getTime();
        timeComparator.isEquals(date1,date2);
    }

    @Test
    public void testGetAll(){
        Set<GameMessage> messages = gameMessageDao.getAll();
        Assertions.assertThat(messages.size()).isEqualTo(this.ids.size());
        messages.forEach(message->{
            Assertions.assertThat(this.messages).contains(message.getMessage());
            Assertions.assertThat(message.getId()).isNotNull();
        });
    }

    @Test
    public void testGetAllBySenderId(){
        String login = "login___1";
        String email = "email___1@mail.com";
        int count = 3;
        long oldPlayerId = -1;

        Player player = creator.createPlayer(login,email);

        for(int i = 0;i<count;i++){
            GameMessage gameMessage = gameMessageDao.getOneById(this.ids.get(i)).orElse(null);
            gameMessage.setSender(player);
            gameMessageDao.saveOrUpdate(gameMessage);
        }

        oldPlayerId = gameMessageDao.getOneById(this.ids.get(count+2)).get().getSender().getId();

        Set<GameMessage> messagesWithNewSender = gameMessageDao.getAllBySenderId(player.getId());
        Set<GameMessage> messagesWithOldSender = gameMessageDao.getAllBySenderId(oldPlayerId);
        Assertions.assertThat(messagesWithNewSender.size()).isEqualTo(count);
        Assertions.assertThat(messagesWithOldSender.size()).isEqualTo(1);
    }
}

package ru.krey.games.dao;

import lombok.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.assertj.core.api.Assertions;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.service.TimeComparator;
import ru.krey.games.domain.Player;
import ru.krey.games.dao.service.Creator;
import ru.krey.games.utils.RoleUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@SpringBootTest
@RunWith(SpringRunner.class)
@NoArgsConstructor
@TestPropertySource("/application-test.properties")
public class PlayerDaoTest {
    @Autowired
    private  PlayerDao playerDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Creator creator;

    @Autowired
    private TimeComparator timeComparator;

    private  List<Pair> players;
    private List<Long> ids;

    @Before
    public void fillPlayers(){
        jdbcTemplate.update("DELETE FROM player");
        players = new ArrayList<>();
        ids = new ArrayList<>();
        for(int i = 0;i<10;i++){
            players.add(new Pair("login"+i,"email"+i+"@mail.ru"));
        }
        players.forEach(p->{
            ids.add(creator.createPlayer(p.login,p.email).getId());
        });
    }

    @After
    public void removePlayers(){
        players.forEach(p->{
            jdbcTemplate.update("DELETE FROM player WHERE login=?",p.login);
        });
    }

    @Test
    public void testAutowired(){
        Assertions.assertThat(playerDao).isNotNull();
    }

    @Test
    public void testSuccessSave(){
        String login = "kirill001";
        Player player = creator.createPlayer(login,"email@email.com");

        Assertions.assertThat(player).isNotNull();
        Assertions.assertThat(player.getId()).isNotNull();

        Player playerFromDb = playerDao.getOneById(player.getId()).orElse(null);
        Assertions.assertThat(playerFromDb).isNotNull();
        Assertions.assertThat(playerFromDb).isEqualTo(player);

        jdbcTemplate.update("DELETE FROM player WHERE login =?",login);
    }

    @Test
    public void testFailedSave(){
        Throwable thrown1 = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,()-> {
                    playerDao.saveOrUpdate(null);
                }
        );
        Assertions.assertThat(thrown1).isInstanceOf(IllegalArgumentException.class);

        String login = "kirill002";
        String email = "k@mail.com";

        creator.createPlayer(login,email);
        Throwable thrown2 = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,()->{
            creator.createPlayer(login,"other@mail.com");
        });
        Assertions.assertThat(thrown2.getMessage()).isNotNull();
        jdbcTemplate.update("DELETE FROM player WHERE login =?",login);

        creator.createPlayer(login,email);
        Throwable thrown3 = org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,()->{
            creator.createPlayer(login+"other",email);
        });
        Assertions.assertThat(thrown3.getMessage()).isNotNull();
        jdbcTemplate.update("DELETE FROM player WHERE email=?",email);
    }

    @Test
    public void testUpdate(){
        Player playerForUpdate = playerDao.getOneById(ids.get(0)).get();
        playerForUpdate.setRating(3);
        playerForUpdate.setRole(RoleUtils.ROLE_ADMIN);
        playerForUpdate.setEnabled(false);
        playerForUpdate.setEmail("new_email@mail.com");
        playerForUpdate.setPhoto("photo");
        playerDao.saveOrUpdate(playerForUpdate);

        Player playerAfterUpdate = playerDao.getOneById(ids.get(0)).get();
        Assertions.assertThat(playerAfterUpdate).isEqualTo(playerForUpdate);

        //check time
        playerForUpdate.setSignUpTime(LocalDateTime.now());
        playerForUpdate.setLastSignInTime(LocalDateTime.now());
        playerDao.saveOrUpdate(playerForUpdate);
        playerAfterUpdate = playerDao.getOneById(ids.get(0)).get();

        timeComparator.isEquals(playerAfterUpdate.getLastSignInTime(),playerForUpdate.getLastSignInTime());
        timeComparator.isEquals(playerAfterUpdate.getSignUpTime(),playerForUpdate.getSignUpTime());
    }

    @Test
    public void testGetAll(){
        Set<Player> players = playerDao.getAll();
        Assertions.assertThat(players.size()).isEqualTo(this.ids.size());
        players.forEach(p->{
            Assertions.assertThat(this.players).contains(new Pair(p.getLogin(),p.getEmail()));
            Assertions.assertThat(p.getId()).isNotNull();
        });
    }

    @Test
    public void testGetOne(){
        Player player1 = playerDao.getOneById(ids.get(0)).orElse(null);
        Assertions.assertThat(player1).isNotNull();
        Assertions.assertThat(player1.getLogin()).isEqualTo(this.players.get(0).login);
        Assertions.assertThat(player1.getEmail()).isEqualTo(this.players.get(0).email);

        Player player2 = playerDao.getOneByEmail(this.players.get(0).email).orElse(null);
        Assertions.assertThat(player2).isNotNull();
        Assertions.assertThat(player2.getId()).isEqualTo(this.ids.get(0));
        Assertions.assertThat(player2.getLogin()).isEqualTo(this.players.get(0).login);


        Player player3 = playerDao.getOneByLogin(this.players.get(0).login).orElse(null);
        Assertions.assertThat(player3).isNotNull();
        Assertions.assertThat(player3.getId()).isEqualTo(this.ids.get(0));
        Assertions.assertThat(player3.getEmail()).isEqualTo(this.players.get(0).email);
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class Pair{
        String login;
        String email;
    }
}

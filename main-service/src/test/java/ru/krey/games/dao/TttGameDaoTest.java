package ru.krey.games.dao;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.service.Creator;
import ru.krey.games.dao.service.TimeComparator;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.games.ttt.TttGame;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@NoArgsConstructor
@TestPropertySource("/application-test.properties")
public class TttGameDaoTest {
    @Autowired
    private TttGameDao tttGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Creator creator;

    @Autowired
    TimeComparator timeComparator;

    private List<Long> ids;
    private List<Integer> sizeFields;
    private List<LoginPair> playerLogins;

    private final int countGamesInit = 10;
    @Before
    public void fill(){
        clear();

        ids = new ArrayList<>();
        sizeFields = new ArrayList<>();
        playerLogins = new ArrayList<>();
        int count = countGamesInit;
        for(int i = 0;i<count;i++){
            sizeFields.add(i+3);
            playerLogins.add(new LoginPair("player1_login"+i,"player2_login"+i));
        }
        //fill db
        for(int i = 0;i<count;i++){
            ids.add(creator.createGame(playerLogins.get(i).login1,"player1_email"+i,
                    playerLogins.get(i).login2,"player2_email"+i, sizeFields.get(i)).getId());
        }
    }

    @After
    public void clear(){
        jdbcTemplate.update("DELETE FROM game_message");
        jdbcTemplate.update("DELETE FROM ttt_game");
        jdbcTemplate.update("DELETE FROM player");
    }

    @Test
    public void testAutowired(){
        assertThat(tttGameDao).isNotNull();
    }

    @Test
    public void testGetOne(){
        TttGame game=  tttGameDao.getOneById(this.ids.get(0)).orElse(null);
        assertThat(game).isNotNull();
        assertThat(game.getSizeField()).isEqualTo(this.sizeFields.get(0));
        assertThat(game.getPlayer1().getLogin()).isEqualTo(this.playerLogins.get(0).login1);
    }

    @Test
    public void testGetAll(){
        Set<TttGame> games = tttGameDao.getAll();
        games.forEach((game)->{
            assertThat(this.playerLogins).contains(new LoginPair(game.getPlayer1().getLogin(), game.getPlayer2().getLogin()));
            assertThat(this.ids).contains(game.getId());
            assertThat(this.sizeFields).contains(game.getSizeField());
        });
    }

    @Test
    public void testSuccessSave(){
        final String login = "login__in__testsave";
        final String email = "email__in__testsave";
        final int size = 3;
        TttGame game = creator.createGame(login,email,null,null,size);
        assertThat(game).isNotNull();
        assertThat(game.getId()).isNotNull();

        TttGame gameFromDb = tttGameDao.getOneById(game.getId()).orElse(null);
        assertThat(gameFromDb).isNotNull();
        assertThat(gameFromDb).isEqualTo(game);
    }


    @Test
    public void testFailedSave(){
        Throwable thrown1 = assertThrows(RuntimeException.class,()->{
            tttGameDao.saveOrUpdate(null);
        });
        assertThat(thrown1).isInstanceOf(IllegalArgumentException.class);

        Throwable thrown2 = assertThrows(Exception.class,()->{
           tttGameDao.saveOrUpdate(creator.createGame(null,null,null,null,33));
        });
        assertThat(thrown2.getMessage()).isNotNull();
    }

    @Test
    public void testUpdate(){
        TttGame tttGame = tttGameDao.getOneById(this.ids.get(0)).orElse(null);

        tttGame.setActualDuration(31);
        tttGame.setPlayer1Time(331L);
        tttGame.setPlayer2Time(332L);
        tttGame.setBasePlayerTime(333L);
        tttGame.setSizeField(99);
        tttGame.setVictoryReasonCode((byte) 222);
        Player winner = creator.createPlayer("login_in_game_update_1","email_in_game_update_1");
        tttGame.setPlayer1(winner);
        tttGame.setPlayer2(creator.createPlayer("login_in_game_update_2","email_in_game_update_2"));
        tttGame.setWinner(winner);
        tttGameDao.saveOrUpdate(tttGame);

        TttGame gameFromDb = tttGameDao.getOneById(this.ids.get(0)).get();
        assertThat(gameFromDb).isEqualTo(tttGame);

        //time
        tttGame.setEndTime(LocalDateTime.now().minusSeconds(3));
        tttGame.setStartTime(LocalDateTime.now().minusSeconds(10));
        tttGameDao.saveOrUpdate(tttGame);

        gameFromDb = tttGameDao.getOneById(this.ids.get(0)).get();

        timeComparator.isEquals(tttGame.getStartTime(),gameFromDb.getStartTime());
        timeComparator.isEquals(tttGame.getEndTime(),gameFromDb.getEndTime());
    }

    @Test
    public void getAllByPlayerId(){
        List<TttGame> games = new ArrayList<>(tttGameDao.getAll());
        Player player1 = creator.createPlayer("login_in_get_all_by_playerId_1","email_in_get_all_by_playerId_1");
        Player player2 = creator.createPlayer("login_in_get_all_by_playerId_2","login_in_get_all_by_playerId_2");
        final int count = countGamesInit-2;

        for(int i = 0;i<count;i++){
            if(i%2==0){
                games.get(i).setPlayer1(player1);
                games.get(i).setWinner(player1);
            }else{
                games.get(i).setPlayer2(player2);
                games.get(i).setWinner(player2);
            }
            tttGameDao.saveOrUpdate(games.get(i));
        }

        games = new ArrayList<>(tttGameDao.getAllByPlayerId(player1.getId()));
        assertThat(games.size()).isEqualTo(count/2);

        games = new ArrayList<>(tttGameDao.getAllByPlayerId(player2.getId()));
        assertThat(games.size()).isEqualTo(count/2);

    }

    @AllArgsConstructor
    @EqualsAndHashCode
    static class LoginPair{
        String login1;
        String login2;
    }

}

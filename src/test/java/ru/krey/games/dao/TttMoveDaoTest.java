package ru.krey.games.dao;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.dao.interfaces.TttGameDao;
import ru.krey.games.dao.interfaces.TttMoveDao;
import ru.krey.games.dao.service.Creator;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.TttGame;
import ru.krey.games.domain.TttMove;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
@NoArgsConstructor
@TestPropertySource("/application-test.properties")
public class TttMoveDaoTest {
    @Autowired
    private TttMoveDao tttMoveDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private TttGameDao tttGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Creator creator;

    private List<Long> ids = new ArrayList<>();
    private List<Coordinates> coordinates = new ArrayList<>();

    private final int countInDb = 10;

    @Before
    public void fill(){
        clear();

        for(int i = 0;i<countInDb;i++){
            coordinates.add(new Coordinates(i,i+1));
        }
        for(int i = 0;i<countInDb;i++){
            ids.add(creator.createMove(coordinates.get(i).x,coordinates.get(i).y).getId());
        }
    }

    @After
    public void clear(){
        jdbcTemplate.update("DELETE FROM player");
        jdbcTemplate.update("DELETE FROM ttt_move");
        jdbcTemplate.update("DELETE FROM ttt_game");
    }

    @Test
    public void testAutowired(){
        assertThat(tttMoveDao).isNotNull();
    }

    @Test
    public void testGetOne(){
        int index = 3;
        TttMove move = tttMoveDao.getOneById(ids.get(index)).orElse(null);
        assertThat(move).isNotNull();
        assertThat(move.getId()).isNotNull();
        assertThat(Coordinates.of(move.getXCoordinate(),move.getYCoordinate())).isEqualTo(coordinates.get(index));
    }

    @Test
    public void testGetAll(){
        //get all
        List<TttMove> allMoves = new ArrayList<>(tttMoveDao.getAll());
        allMoves.forEach(move->{
            assertThat(move.getId()).isNotNull();
            assertThat(this.coordinates).contains(Coordinates.of(move.getXCoordinate(),move.getYCoordinate()));
        });

        int countInSample  = countInDb - 4;

        //get all by player id
        creator.createMoveWithoutPlayer(10,10);
        creator.createMoveWithoutPlayer(20,20);
        Player player1 = creator.createPlayerSave("login_for_get_all_moves","email_for_get_all_moves@mail.com");
        for (int i = 0;i<countInSample;i++){
            allMoves.get(i).setPlayer(player1);
            allMoves.get(i).getGame().setPlayer1(player1);
            tttMoveDao.saveOrUpdate(allMoves.get(i));
            tttGameDao.saveOrUpdate(allMoves.get(i).getGame());
        }
        Set<TttMove> moves =  tttMoveDao.getAllByPlayerId(player1.getId());
        assertThat(moves.size()).isEqualTo(countInSample);

        //get all by game id
        TttGame game = creator.createGame("login_for_get_all_moves_in_game","email_for_get_all_moves_in_game@mail.com",null,null,4);
        for(int i = 0;i<countInSample;i++){
            allMoves.get(i).setGame(game);
            tttMoveDao.saveOrUpdate(allMoves.get(i));
        }
        moves = tttMoveDao.getAllByGameId(game.getId());
        assertThat(moves.size()).isEqualTo(countInSample);
    }

    @Test
    public void testUpdate(){
        TttMove move = tttMoveDao.getOneById(ids.get(0)).get();
        move.setGame(creator.createGame("login_in_success_update_for_move","email_in_success_update_for_move@mail.com",null,null,5));
        move.setPlayer(playerDao.getOneByLogin("login_in_success_update_for_move").get());
        move.setXCoordinate(21);
        move.setYCoordinate(112);
        move.setGameTimeMillis(1111);
        move.setAbsoluteTime(LocalDateTime.of(2020,12,12,12,12,12));
        tttMoveDao.saveOrUpdate(move);

        TttMove moveFromDb = tttMoveDao.getOneById(ids.get(0)).get();
        assertThat(moveFromDb).isEqualTo(move);
    }

    @Test
    public void testFailedSave(){
        Throwable thrown1 = assertThrows(RuntimeException.class,()->{
            tttMoveDao.saveOrUpdate(null);
        });
        assertThat(thrown1).isInstanceOf(IllegalArgumentException.class);
        Throwable thrown2 = assertThrows(Exception.class,()->{
            tttMoveDao.saveOrUpdate(TttMove.builder().build());
        });
        assertThat(thrown2.getMessage()).isNotNull();
    }

    @Test
    public void testSuccessSave(){
        TttMove move = creator.createMove(0,0);
        assertThat(move).isNotNull();
        assertThat(move.getId()).isNotNull();

        TttMove moveFromDb = tttMoveDao.getOneById(move.getId()).orElse(null);
        assertThat(moveFromDb).isNotNull();
        assertThat(moveFromDb).isEqualTo(move);
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    static class Coordinates{
        Integer x;
        Integer y;
        static Coordinates of(int x,int y){
            return new Coordinates(x,y);
        }
    }

}

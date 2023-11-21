package ru.krey.games.playerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.playerservice.dao.interfaces.PlayerDao;
import ru.krey.games.playerservice.domain.Player;
import ru.krey.games.playerservice.error.BadRequestException;
import ru.krey.games.playerservice.error.NotFoundException;
import ru.krey.games.playerservice.service.interfaces.FileService;
import ru.krey.games.playerservice.service.interfaces.GameService;
import ru.krey.games.playerservice.service.interfaces.PlayerService;
import ru.krey.libs.securitylib.utils.RoleUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    private final PlayerDao playerDao;

    private final PasswordEncoder passwordEncoder;

    private final FileService fileService;

    private final GameService gameService;

    @Override
    public void updateActive(String username) {
        playerDao.updateActive(username);
    }

    @Override
    public List<Player> getActivePlayers() {
        return playerDao.getActivePlayersByTimeDiff(30);
    }

    @Override
    public Player getOneById(Long id) {
        Player player = playerDao.getOneById(id)
                .orElseThrow(() -> new NotFoundException("Игрока с таким id не существует!"));
        player.setPassword(null);
        return player;
    }

    @Override
    public Player getOneByLogin(String login) {
        Player player = playerDao.getOneByLogin(login)
                .orElseThrow(() -> new BadRequestException("Игрока с таким логином нет!"));
        return player;
    }

    @Override
    public Long getCurrentGameId(Long playerId) {
        Player player = getOneById(playerId);
        return gameService.getCurrentGameId(playerId, player.getLastGameCode());
    }

    @Override
    public List<Player> getAllOrderedByRating() {
        return playerDao.getAllOrderByRating();
    }

    @Override
    public List<Player> getPartOrderedByRating(Long from, Long to) {
        List<Player> players = getAllOrderedByRating();
        if (from >= players.size()) {
            return new ArrayList<>();
        }
        return players.subList(Math.min(players.size() - 1, from.intValue()), Math.min(players.size(), to.intValue()));
    }

    @Override
    public Long getPlayerTopById(Long id) {
        return playerDao.getPlayerTopById(id);
    }

    @Override
    public List<Player> search(String search, int from, int to) {
        List<Player> result = new ArrayList<>();

        Consumer<Player> addIfNotContains = (player) -> {
            if (!result.contains(player)) {
                result.add(player);
            }
        };

        playerDao.getOneByLogin(search).ifPresent(addIfNotContains);
        playerDao.getOneByEmail(search).ifPresent(addIfNotContains);

        playerDao.getPlayersWithNameStarts(search.toLowerCase()).forEach(addIfNotContains);
        playerDao.getPlayersByPartOfName(search.toLowerCase()).forEach(addIfNotContains);
        playerDao.getPlayersByPartOfEmail(search.toLowerCase()).forEach(addIfNotContains);

        if (result.isEmpty()) {
            return result;
        }
        return result.subList(Math.min(result.size() - 1, from), Math.min(result.size(), to));
    }

    @Override
    public Player updatePlayer(String login, String email, String password, Long id, MultipartFile img) {
        Player playerFromDb = playerDao.getOneById(id)
                .orElseThrow(() -> new NotFoundException("Игрока с таким id не существует!"));

        checkLoginAndEmail(login,email,playerFromDb);
        playerFromDb.setLogin(login);
        playerFromDb.setEmail(email);

        if (password != null && !password.isBlank()) {
            playerFromDb.setPassword(passwordEncoder.encode(password));
        }

        return savePlayer(playerFromDb, img);
    }

    @Override
    public Player updatePlayer(Player player) {
        Player playerFromDb = playerDao.getOneById(player.getId())
                .orElseThrow(() -> new NotFoundException("Игрока с таким id не существует!"));
        checkLoginAndEmail(player.getLogin(), player.getEmail(), playerFromDb);
        if (player.getPassword() != null && !player.getPassword().isBlank()) {
            playerFromDb.setPassword(passwordEncoder.encode(player.getPassword()));
        }else{
            player.setPassword(playerFromDb.getPassword());
        }
        return playerDao.saveOrUpdate(player);
    }

    @Override
    public Player createPlayer(MultipartFile image, String login, String email, String password) {
        Player player = buildPlayer(login, email, password);
        checkLoginOrEmailAlreadyExists(player.getLogin(), player.getEmail());
        return savePlayer(player, image);
    }

    @Override
    public Set<Player> getPlayersByIds(List<Long> ids) {
        return playerDao.getPlayersByIds(ids);
    }

    private Player savePlayer(Player player, MultipartFile image) {
        player = playerDao.saveOrUpdate(player);
        if (Objects.nonNull(image)) {
            try {
                String fileName = fileService.saveImage(image, player.getLogin(), player.getId());
                player.setPhoto(fileName);
            } catch (Exception e) {
                throw new RuntimeException("Не удалось загрузить фото. Попробуйте снова в профиле.", e);
            }
        }
        player = playerDao.saveOrUpdate(player);
        player.setPassword(null);
        return player;
    }

    private Player buildPlayer(String login, String email, String password) {
        return Player.builder()
                .login(login)
                .email(email)
                .password(passwordEncoder.encode(password))
                .lastSignInTime(LocalDateTime.now())
                .signUpTime(LocalDateTime.now())
                .enabled(true)
                .rating(0)
                .role(RoleUtils.ROLE_USER)
                .build();
    }

    private void checkLoginOrEmailAlreadyExists(String login, String email) {
        List<Player> playerList = playerDao.getPlayersByLoginOrEmail(login, email);
        playerList.stream().findAny().ifPresent((pl) -> {
            if (pl.getLogin().equals(login))
                throw new BadRequestException("Пользователь с таким логином уже существует!");
            else throw new BadRequestException("Пользователь с таким email уже существует!");
        });
    }

    private void checkLoginAlreadyExists(String login) {
        Optional<Player> players = playerDao.getOneByLogin(login);
        players.ifPresent((pl) -> {
            throw new BadRequestException("Пользователь с таким логином уже существует!");
        });
    }

    private void checkEmailAlreadyExists(String email) {
        Optional<Player> players = playerDao.getOneByEmail(email);
        players.ifPresent((pl) -> {
            throw new BadRequestException("Пользователь с таким email уже существует!");
        });
    }

    private void checkLoginAndEmail(String login, String email, Player player){
        if (!player.getEmail().equals(email)) {
            checkEmailAlreadyExists(email);
        }
        if (!player.getLogin().equals(login)) {
            checkLoginAlreadyExists(login);
        }
    }
}

package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Player;
import ru.krey.games.domain.interfaces.Game;
import ru.krey.games.error.BadRequestException;
import ru.krey.games.error.NotFoundException;
import ru.krey.games.service.interfaces.ImageService;
import ru.krey.games.utils.GameUtils;
import ru.krey.lib.securitylib.utils.RoleUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PlayerService implements UserDetailsService {

    private final PlayerDao playerDao;

    private final PasswordEncoder passwordEncoder;

    private final ImageService imageService;

    private final TttGameService tttGameService;

    private final TetrisService tetrisService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getOneByLogin(username);
    }

    public void updateActive(String username) {
        playerDao.updateActive(username);
    }

    public List<Player> getActivePlayers() {
        return playerDao.getActivePlayersByTimeDiff(30);
    }

    public Player getOneById(Long id) {
        Player player = playerDao.getOneById(id)
                .orElseThrow(() -> new NotFoundException("Игрока с таким id не существует!"));
        player.setPassword(null);
        return player;
    }

    public Player getOneByLogin(String login) {
        Player player = playerDao.getOneByLogin(login)
                .orElseThrow(() -> new BadRequestException("Авторизованного игрока нет!"));
        return player;
    }

    public Optional<? extends Game> getCurrentGame(Long playerId) {
        Player player = getOneById(playerId);
        return getCurrentGame(player);
    }

    public Optional<? extends Game> getCurrentGame(Player player) {
        if (player.getLastGameCode() == GameUtils.TTT_GAME_CODE) {
            return tttGameService.getCurrentGameByPlayerId(player.getId());
        }

        if (player.getLastGameCode() == GameUtils.TETRIS_GAME_CODE) {
            return tetrisService.getCurrentGameByPlayerId(player.getId());
        }

        return Optional.empty();
    }

    public List<Player> getAllOrderedByRating() {
        return playerDao.getAllOrderByRating();
    }

    public List<Player> getPartOrderedByRating(Long from, Long to) {
        List<Player> players = getAllOrderedByRating();
        if (from >= players.size()) {
            return new ArrayList<>();
        }
        return players.subList(Math.min(players.size() - 1, from.intValue()), Math.min(players.size(), to.intValue()));
    }

    public Long getPlayerTopById(Long id) {
        return playerDao.getPlayerTopById(id);
    }

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

    public Player updatePlayer(String login, String email, String password, Long id, MultipartFile img) {
        Player playerFromDb = getOneById(id);

        checkAlreadyExists(login, email);

        playerFromDb.setLogin(login);
        playerFromDb.setEmail(email);

        if (password != null && !password.isBlank()) {
            playerFromDb.setPassword(passwordEncoder.encode(password));
        }

        Player player = playerDao.saveOrUpdate(playerFromDb);
        playerFromDb.setPassword(null);

        if (img != null && !img.isEmpty()) {
            try {
                imageService.savePlayerImage(img, playerFromDb.getId());
            } catch (IOException e) {
                throw new RuntimeException("Не удалось загрузить фото. Попробуйте снова в профиле.", e);
            }
        }
        player.setPassword(null);
        return player;
    }

    public Player createPlayer(MultipartFile image, String login, String email, String password) {

        Player player = buildPlayer(image, login, email, password);

        checkAlreadyExists(login, email);

        player = playerDao.saveOrUpdate(player);

        if (Objects.nonNull(image)) {
            try {
                imageService.savePlayerImage(image, player.getId());
            } catch (IOException e) {
                throw new RuntimeException("Не удалось загрузить фото. Попробуйте снова в профиле.", e);
            }
        }
        player.setPassword(null);

        return player;
    }

    private Player buildPlayer(MultipartFile image, String login, String email, String password) {
        return Player.builder()
                .login(login)
                .email(email)
                .password(passwordEncoder.encode(password))
                .lastSignInTime(LocalDateTime.now())
                .signUpTime(LocalDateTime.now())
                .enabled(true)
                .rating(0)
                .Role(RoleUtils.ROLE_USER)
                .build();
    }

    private void checkAlreadyExists(String login, String email) {
        List<Player> playerList = playerDao.getPlayersByLoginOrEmail(login, email);
        playerList.stream().findAny().ifPresent((pl) -> {
            if (pl.getLogin().equals(login))
                throw new BadRequestException("Пользователь с таким логином уже существует!");
            else throw new BadRequestException("Пользователь с таким email уже существует!");
        });
    }
}

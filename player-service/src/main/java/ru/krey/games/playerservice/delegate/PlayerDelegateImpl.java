package ru.krey.games.playerservice.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.playerservice.domain.Player;
import ru.krey.games.playerservice.error.BadRequestException;
import ru.krey.games.playerservice.openapi.api.PlayerApiDelegate;
import ru.krey.games.playerservice.openapi.model.JwtResponseOpenApi;
import ru.krey.games.playerservice.openapi.model.PlayerOpenApi;
import ru.krey.games.playerservice.service.PlayerServiceImpl;
import ru.krey.games.playerservice.service.interfaces.AuthService;
import ru.krey.games.playerservice.utils.mapper.mapstruct.PlayerMapper;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlayerDelegateImpl implements PlayerApiDelegate {

    private final PlayerServiceImpl playerService;

    private final AuthService authService;

    private final PlayerMapper playerMapper;

    @Override
    public ResponseEntity<Void> createPlayer( PlayerOpenApi player,
                                              MultipartFile img) {
        if (player.getLogin() == null || player.getLogin().isBlank() ||
                player.getEmail() == null || player.getEmail().isBlank() ||
                player.getPassword() == null || player.getPassword().isBlank()) {
            throw new BadRequestException("Не все поля заполнены.");
        }
        playerService.createPlayer(img, player.getLogin(), player.getEmail(), player.getPassword());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<JwtResponseOpenApi> updatePlayer(String login,
                                                           String email,
                                                           Long id,
                                                           String password,
                                                           MultipartFile img) {
        String playerName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (login == null || login.isBlank() ||
                email == null || email.isBlank()) {
            throw new BadRequestException("Не все поля заполнены.");
        }
        Player playerFromDb = playerService.getOneByLogin(playerName);
        if (Objects.isNull(playerFromDb) || !playerFromDb.getId().equals(id)) {
            throw new BadRequestException("Нет доступа к изменению пользователя с id = " + id);
        }
        Player player = playerService.updatePlayer(login, email, password, id, img);
        player.setPassword(null);
        return ResponseEntity.ok(
                new JwtResponseOpenApi(authService.createAuthToken(player),
                        playerMapper.toOpenApi(player))
        );
    }

    @Override
    public ResponseEntity<PlayerOpenApi> savePlayer(PlayerOpenApi playerOpenApi) {
        return ResponseEntity.ok(
                playerMapper.toOpenApi(
                        playerService
                                .updatePlayer(playerMapper.toPlayer(playerOpenApi))
                )
        );
    }

    @Override
    public ResponseEntity<List<PlayerOpenApi>> getAllOrderedByRatingStepByStep(Long from, Long to) {
        return ResponseEntity.ok(
                playerService.getPartOrderedByRating(from, to)
                        .stream()
                        .map(playerMapper::toOpenApi)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<PlayerOpenApi> getAuthenticatedUser() {
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userName == null) {
            throw new BadRequestException("Авторизованного игрока нет!");
        }
        return ResponseEntity.ok(
                playerMapper.toOpenApi(playerService.getOneByLogin(userName))
        );
    }

    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<PlayerOpenApi> getById(Long id) {
        return ResponseEntity.ok(
                playerMapper.toOpenApi(playerService.getOneById(id))
        );
    }

    @Override
    public ResponseEntity<PlayerOpenApi> getByLogin(String login) {
        return ResponseEntity.ok(
                playerMapper.toOpenApi(playerService.getOneByLogin(login))
        );
    }

    @Override
    public ResponseEntity<Long> getCurrentGameCode(Long playerId) {
        return ResponseEntity.ok(
                playerService.getOneById(playerId).getLastGameCode().longValue()
        );
    }


    @Override
    public ResponseEntity<Long> getCurrentGameId(Long playerId) {
        return ResponseEntity.ok(
                playerService.getCurrentGameId(playerId)
        );
    }

    @Override
    public ResponseEntity<Long> getPlayerTop(Long id) {
        return ResponseEntity.ok(
                playerService.getPlayerTopById(id)
        );
    }

    @Override
    public ResponseEntity<List<PlayerOpenApi>> searchPlayers(String searchQuery,
                                                             Long from,
                                                             Long to) {
        if (from > to) {
            throw new BadRequestException("from>to");
        }
        if (searchQuery.isBlank()) {
            return ResponseEntity.ok(List.of());
        }
        searchQuery = searchQuery.trim();
        return ResponseEntity.ok(
                playerService.search(searchQuery, from.intValue(), to.intValue())
                        .stream()
                        .map(playerMapper::toOpenApi)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<List<PlayerOpenApi>> getActivePlayers() {
        return ResponseEntity.ok(playerMapper.toOpenApi(playerService.getActivePlayers()));
    }

    @Override
    public ResponseEntity<Void> updateActive() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (username == null) {
            throw new BadRequestException("Нет авторизованного пользователя!");
        }
        playerService.updateActive(username);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Map<String, PlayerOpenApi>> getPlayersByIds(List<Long> ids) {
        Set<PlayerOpenApi> players = playerMapper.toOpenApi(playerService.getPlayersByIds(ids));
        Map<String, PlayerOpenApi> map = new HashMap<>();
        players.forEach(player->map.put(player.getId().toString(), player));
        return ResponseEntity.ok(map);
    }
}

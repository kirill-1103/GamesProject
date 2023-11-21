package ru.krey.games.playerservice.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import ru.krey.games.playerservice.domain.Player;

import java.util.List;
import java.util.Set;

public interface PlayerService {
    void updateActive(String username);

    List<Player> getActivePlayers();

    Player getOneById(Long id);

    Player getOneByLogin(String login);

    Long getCurrentGameId(Long playerId);

    List<Player> getAllOrderedByRating();

    List<Player> getPartOrderedByRating(Long from, Long to);

    Long getPlayerTopById(Long id);

    List<Player> search(String search, int from, int to);

    Player updatePlayer(String login, String email, String password, Long id, MultipartFile img);

    Player createPlayer(MultipartFile image, String login, String email, String password);

    Player updatePlayer(Player player);

    Set<Player> getPlayersByIds(List<Long> ids);
}

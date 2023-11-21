package ru.krey.games.playerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.krey.games.playerservice.constant.ServicesNames;
import ru.krey.games.playerservice.service.interfaces.GameService;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final RestTemplate restTemplate;
    private final static String GET_CURRENT_GAME_URL = String.format("http://%s/api/games/current/{player_id}/{game_code}", ServicesNames.MAIN_SERVICE);

    @Override
    public Long getCurrentGameId(Long playerId, Integer lastGameCode) {
        try {
            ResponseEntity<Long> result = restTemplate.getForEntity(GET_CURRENT_GAME_URL, Long.class, playerId, lastGameCode);
            if (result.getStatusCode().equals(HttpStatus.OK)) {
                return result.getBody();
            }
        } catch (Exception e) {
            log.error(e.getMessage());        }
        return null;
    }
}

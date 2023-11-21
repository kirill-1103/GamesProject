package ru.krey.games.playerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.krey.games.playerservice.constant.ServicesNames;
import ru.krey.games.playerservice.domain.Player;
import ru.krey.games.playerservice.error.BadRequestException;
import ru.krey.games.playerservice.service.interfaces.AuthService;
import ru.krey.libs.securitylib.filter.JwtRequestFilter;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final static String GET_TOKEN_URL = String.format("http://%s/auth/get-token", ServicesNames.AUTH_SERVICE);

    private final RestTemplate restTemplate;

    @Override
    public String createAuthToken(Player player) {
        try {
            ResponseEntity<String> token = restTemplate.postForEntity(GET_TOKEN_URL, player, String.class);
            return token.getBody();
        } catch (Exception e) {
            log.error("Не удалось получить токен. Exception: " + e.getMessage());
            throw new BadRequestException("Не удалось получить токен");
        }
    }
}

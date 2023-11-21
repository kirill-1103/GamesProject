package ru.krey.games.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.krey.games.openapi.ApiClient;
import ru.krey.games.openapi.api.PlayerApi;
import ru.krey.games.utils.jwt.TokenKeeper;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApiClientConfig {

    private final TokenKeeper tokenKeeper;

    @Bean
    public ApiClient userClient() {
        ApiClient apiClient = ru.krey.games.openapi.Configuration.getDefaultApiClient();
        apiClient.setRequestInterceptor((http) -> {
            http.setHeader(
                    "Authorization",
                    "Bearer "
                            + tokenKeeper.getToken("ADMIN", List.of("ADMIN"))
            );
        });
        return apiClient;
    }

    @Bean
    public PlayerApi playerApi(ApiClient apiClient) {
        PlayerApi playerApi = new PlayerApi(apiClient);
        return playerApi;
    }
}

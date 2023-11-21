package ru.krey.games.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import ru.krey.games.authservice.constant.ServicesNames;
import ru.krey.games.openapi.ApiClient;
import ru.krey.games.openapi.api.PlayerApi;
import ru.krey.libs.jwtlib.utils.state.TokenKeeper;
import ru.krey.libs.securitylib.utils.RoleUtils;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApiClientConfig {

    private final TokenKeeper tokenKeeper;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate r = new RestTemplate();
        r.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().set("Authorization", "Bearer "+tokenKeeper.getToken(
                    ServicesNames.ME, List.of(RoleUtils.ROLE_ADMIN)
            ));
            return execution.execute(request,body);
        }));
        return r;
    }
    @Bean
    @Primary
    public ApiClient userClient(RestTemplate restTemplate) {
        ApiClient apiClient = new ApiClient(restTemplate);
        return apiClient;
    }
    @Bean
    public PlayerApi playerApi(ApiClient apiClient) {
        PlayerApi playerApi = new PlayerApi(apiClient);
        return playerApi;
    }
}

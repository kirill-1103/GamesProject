package ru.krey.games.playerservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.krey.games.playerservice.constant.ServicesNames;
import ru.krey.libs.jwtlib.utils.state.TokenKeeper;
import ru.krey.libs.securitylib.utils.RoleUtils;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {
    private final TokenKeeper tokenKeeper;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate r = new RestTemplate();
        r.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().set("Authorization", "Bearer " + tokenKeeper.getToken(
                    ServicesNames.ME, List.of(RoleUtils.ROLE_ADMIN)
            ));
            return execution.execute(request, body);
        }));
        return r;
    }
}

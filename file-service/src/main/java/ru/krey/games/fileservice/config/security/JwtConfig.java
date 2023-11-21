package ru.krey.games.fileservice.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.krey.libs.jwtlib.utils.JwtUtils;

@Component
public class JwtConfig {
    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }
}

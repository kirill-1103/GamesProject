package ru.krey.games.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.krey.libs.jwtlib.utils.JwtUtils;

@Configuration
public class JwtConfig {
    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }
}

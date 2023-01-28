package ru.krey.games.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.krey.games.handler.auth.AuthFilter;

@Configuration
@RequiredArgsConstructor
public class AuthFilterConfig {
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationManager authenticationManager;
    @Bean
    public AuthFilter authenticationFilter(){
        AuthFilter authFilter = new AuthFilter();
        authFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        authFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authFilter.setAuthenticationManager(authenticationManager);
        return authFilter;
    }
}

package ru.krey.games.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.krey.games.handler.auth.AuthFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthConfig {
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final String sqlUserName = "SELECT login, password, enabled FROM player WHERE login = ?";
        final String sqlAuthorities = "SELECT login,role FROM player WHERE login = ?";

        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(sqlUserName)
                .authoritiesByUsernameQuery(sqlAuthorities).passwordEncoder(passwordEncoder);
    }


}

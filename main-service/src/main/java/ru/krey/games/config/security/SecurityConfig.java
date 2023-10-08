package ru.krey.games.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import ru.krey.games.handler.auth.AuthFailureHandler;
import ru.krey.games.utils.RoleUtils;


@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userService;

    private final JwtRequestFilter jwtRequestFilter;

    private final AuthFailureHandler failureHandler;


    private static final String[] PUBLIC = new String[]{
            "/error", "/login**", "/auth", "/auth/**", "/register", "/logout", "/registration", "/api/player/authenticated", "/api/settings/**," +
            "/ws/**", "/websocket/**", "/socket/**", "/topic/**", "/game/**", "/game/ttt", "/player/**", "/me**", "/rating**", "/chat/**", "/game_list**", "/player_list**",
            "/api/settings/**", "/"
    };

    private static final String[] FOR_AUTHORIZED = new String[]{
            "/api/**", "/updateToken"
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.headers()
                .addHeaderWriter(
                        new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC)
                .permitAll()
                .antMatchers(FOR_AUTHORIZED)
                .hasAnyAuthority(RoleUtils.getRoleForConfig(RoleUtils.ROLE_USER),
                        RoleUtils.getRoleForConfig(RoleUtils.ROLE_ADMIN))
                .anyRequest()
                .hasAuthority(RoleUtils.ROLE_ADMIN)
                .and()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(failureHandler)
                .accessDeniedPage("/me")
                .and()
                .userDetailsService(userService)
                .exceptionHandling()
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/static/**", "/js/**", "/css/**", "/images/**", "/favicon.ico");
    }

}

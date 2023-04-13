package ru.krey.games.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.krey.games.handler.auth.AuthSuccessHandler;
import ru.krey.games.utils.RoleUtils;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthSuccessHandler authSuccessHandler;

    private static final String[] PUBLIC = new String[]{
            "/error", "/login**","/auth", "/register", "/logout","/registration","/api/player/authenticated","/api/settings/**," +
            "/ws/**","/websocket/**","/socket/**","/topic/**"
    };

    private static final String[] FOR_AUTHORIZED = new String[]{
            "/me**", "/rating**","/chat**","/game_list**","/player_list**","/api/**"
            ,"/game/**","/game/ttt","/player/**"
    };




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .headers()
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
                    .formLogin()
                    .loginPage("/auth")
                    .defaultSuccessUrl("/me")
                    .loginProcessingUrl("/login")
                    .usernameParameter("login")
                    .passwordParameter("password")
                    .successHandler(authSuccessHandler)
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/auth")
                    .permitAll()
                .and()
                    .sessionManagement()
                    .maximumSessions(1)
//                    .maxSessionsPreventsLogin(true)
                .and()
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/me")
                .and()
                .csrf().disable()
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)-> web.ignoring().antMatchers("/static/**","/js/**","/css/**","/images/**","/favicon.ico");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

package ru.krey.lib.securitylib.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.krey.lib.securitylib.filter.JwtRequestFilter;
import ru.krey.lib.securitylib.handler.AuthFailureHandler;
import ru.krey.lib.securitylib.interfaces.PathUtils;
import ru.krey.lib.securitylib.utils.RoleUtils;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final PathUtils pathUtils;
    private final AuthFailureHandler authFailureHandler;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        if(pathUtils.getPublic().length != 0){
            http.authorizeRequests()
                    .antMatchers(pathUtils.getPublic())
                    .permitAll();
        }
        if(pathUtils.getForAuthorized().length != 0){
            http.authorizeRequests()
                    .antMatchers(pathUtils.getForAuthorized())
                    .authenticated();
//                    .hasAnyAuthority(RoleUtils.getRoleForConfig(RoleUtils.ROLE_USER),
//                            RoleUtils.getRoleForConfig(RoleUtils.ROLE_ADMIN));
        }
        if(pathUtils.getForAdmin().length != 0){
            http.authorizeRequests()
                    .antMatchers(pathUtils.getForAuthorized())
                    .hasAnyAuthority(RoleUtils.getRoleForConfig(RoleUtils.ROLE_USER),
                            RoleUtils.getRoleForConfig(RoleUtils.ROLE_ADMIN));
        }
        if(pathUtils.getForUser().length != 0){
            http.authorizeRequests()
                    .antMatchers(pathUtils.getForUser())
                    .hasAnyAuthority(RoleUtils.ROLE_USER);
        }
        return http
                .formLogin().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(authFailureHandler)
                    .accessDeniedPage("/me")
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

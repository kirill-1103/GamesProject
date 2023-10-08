package ru.krey.games.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
public class SpringJdbcConfig {
    private final Environment env;

    public SpringJdbcConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource postgresqlDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("spring.datasource.driver-class-name")));
        dataSource.setUrl(Objects.requireNonNull(env.getProperty("spring.datasource.url")));
        dataSource.setUsername(Objects.requireNonNull(env.getProperty("spring.datasource.username")));
        dataSource.setPassword(Objects.requireNonNull(env.getProperty("spring.datasource.password")));

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(postgresqlDataSource());
    }
}

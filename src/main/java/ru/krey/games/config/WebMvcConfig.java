package ru.krey.games.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.krey.games.dto.converter.TttGameToDtoConverter;
import ru.krey.games.dto.converter.TttMoveDtoToTttMoveConverter;
import ru.krey.games.dto.converter.TttMoveToTttMoveDtoConverter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.krey.games")
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TttMoveDtoToTttMoveConverter tttMoveDtoToTttMoveConverter;

    private final TttMoveToTttMoveDtoConverter tttMoveToTttMoveDtoConverter;

    private final TttGameToDtoConverter tttGameToDto;

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerCustomizer(){
        return factory -> factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/"));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(tttMoveDtoToTttMoveConverter);
        registry.addConverter(tttMoveToTttMoveDtoConverter);
        registry.addConverter(tttGameToDto);
    }
}

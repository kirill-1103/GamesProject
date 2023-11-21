package ru.krey.games.fileservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import ru.krey.games.fileservice.service.ImageServiceLocal;

@Configuration
public class FileConfig {
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(ImageServiceLocal.IMG_MAX_SIZE);
        return multipartResolver;
    }

}
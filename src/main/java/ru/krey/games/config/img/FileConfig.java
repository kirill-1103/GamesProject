package ru.krey.games.config.img;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import ru.krey.games.service.LocalImageService;

@Configuration
@ComponentScan
public class FileConfig {
//    @Bean
//    MultipartConfigElement multipartConfigElement(){
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setMaxFileSize(DataSize.parse("1MB"));
//        factory.setMaxRequestSize(DataSize.parse("1MB"));
//        return factory.createMultipartConfig();
//    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(LocalImageService.max_size);
        return multipartResolver;
    }
//
//    @Bean
//    public StandardServletMultipartResolver multipartResolver() {
//        return new StandardServletMultipartResolver();
//    }
}

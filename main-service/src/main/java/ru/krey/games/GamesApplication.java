package ru.krey.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"ru.krey.games", "ru.krey.libs"})
@EnableScheduling
@EnableEurekaClient
public class GamesApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(GamesApplication.class, args);
	}
}

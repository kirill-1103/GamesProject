package ru.krey.games.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpoint() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket/ttt_new_game", "/socket/ttt_search", "/socket/game_messages", "/socket/chat", "/socket/tetris_game", "/socket/tetris_search")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/websocket");
    }
}


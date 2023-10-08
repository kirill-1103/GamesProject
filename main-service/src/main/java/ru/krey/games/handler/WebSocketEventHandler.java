package ru.krey.games.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventHandler {

    Logger log = LoggerFactory.getLogger(WebSocketEventHandler.class);

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event){
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser() == null ? "empty name" : headers.getUser().getName();

        log.debug("WS Connected: (name:"+username+","+" sessionId:"+headers.getSessionId()+")");
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event){
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser() == null ? "empty name" : headers.getUser().getName();

        log.debug("WS Disconnected: (name:"+username+","+" sessionId:"+headers.getSessionId()+")");
    }
}

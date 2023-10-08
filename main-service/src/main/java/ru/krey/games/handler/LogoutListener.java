package ru.krey.games.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

/*
TODO: с этим тоже что-то делать :(
*  */
@Component
@RequiredArgsConstructor
public class LogoutListener implements ApplicationListener<LogoutSuccessEvent> {
    private final SessionRegistry sessionRegistry;

    private final static Logger log = LoggerFactory.getLogger(LogoutListener.class);

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event){
        Authentication authentication = event.getAuthentication();
        if(authentication!=null){
            Object principal = authentication.getPrincipal();
            if(principal instanceof User){
                List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal,false);
                if(sessions!=null){
                    for(SessionInformation session : sessions){
                        sessionRegistry.removeSessionInformation(session.getSessionId());
                    }
                }
            }
        }
    }
}

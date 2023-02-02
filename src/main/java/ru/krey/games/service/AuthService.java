package ru.krey.games.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.krey.games.domain.Player;

import java.util.Collection;

@Service
public class AuthService {
    public String getCurrentUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
    public void changeSessionUser(Player player){
        if(player.getLogin()==null || player.getPassword()==null){
            throw new RuntimeException("Player must have login and password!");
        }
        Collection<SimpleGrantedAuthority> nowAuthorities =
                (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(player.getLogin(),player.getPassword(),nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}



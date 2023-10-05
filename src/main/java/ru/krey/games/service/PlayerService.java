package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Player;
import ru.krey.games.error.NotFoundException;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerService implements UserDetailsService {

    private final PlayerDao playerDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return playerDao.getOneByLogin(username)
                .orElseThrow(()->{throw new UsernameNotFoundException(String.format("User %s not found", username));});
    }

}

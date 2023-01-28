package ru.krey.games.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//@Service
//@RequiredArgsConstructor
public class PlayerAuthService implements UserDetailsService {
//    private final PlayerDao playerDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        UserDetails userDetails = playerDao.getOneByLogin(username);
//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userDetails.get());
        return null;
    }
}

package ru.krey.games.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.krey.games.dto.AuthDto;
import ru.krey.games.error.SecurityAuthenticationException;
import ru.krey.libs.jwtlib.utils.JwtUtils;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {


    private final JwtUtils jwtUtils;

    private final AuthenticationManager authManager;

    private final PlayerService playerService;

    public ResponseEntity<?> createAuthTokenAndAuthorized(AuthDto authDto){
        try{
            authManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getLogin(),authDto.getPassword()));
            log.info(String.format("USER %s has logged in",authDto.getLogin()));
        } catch (BadCredentialsException e){
            throw new SecurityAuthenticationException("Неверный логин или пароль.");
        }
        return createAuthToken(authDto);
    }

    public ResponseEntity<?> createAuthToken(AuthDto authDto){
        UserDetails userDetails = playerService.loadUserByUsername(authDto.getLogin());
        String token = jwtUtils.generateToken(userDetails.getUsername(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}

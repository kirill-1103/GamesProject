package ru.krey.games.handler.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.krey.games.dao.interfaces.PlayerDao;
import ru.krey.games.domain.Player;
import ru.krey.games.error.NotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final static Logger log = LoggerFactory.getLogger(AuthSuccessHandler.class);
    private final PlayerDao playerDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String login = userDetails.getUsername();

        log.info("The user "+login+" has logged in.");

        Player player = playerDao.getOneByLogin(login)
                .orElseThrow(()->new NotFoundException("Player must exist in this context!"));

        player.setLastSignInTime(LocalDateTime.now());

        playerDao.saveOrUpdate(player);

        this.setDefaultTargetUrl("/me");

        super.onAuthenticationSuccess(request, response, authentication);
    }
}

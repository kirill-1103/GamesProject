package ru.krey.games.utils;

import org.springframework.stereotype.Component;
import ru.krey.lib.securitylib.interfaces.PathUtils;

@Component
public class PathUtilsImpl implements PathUtils {

    private static final String[] PUBLIC = new String[]{
            "/error", "/login**", "/auth", "/auth/**", "/register", "/logout", "/registration", "/api/player/authenticated", "/api/settings/**," +
            "/ws/**", "/websocket/**", "/socket/**", "/topic/**", "/game/**", "/game/ttt", "/player/**", "/me**", "/rating**", "/chat/**", "/game_list**", "/player_list**",
            "/api/settings/**", "/"
    };

    private static final String[] FOR_AUTHORIZED = new String[]{
            "/api/**", "/updateToken"
    };
    @Override
    public String[] getPublic() {
        return PUBLIC;
    }

    @Override
    public String[] getForAuthorized() {
        return FOR_AUTHORIZED;
    }

    @Override
    public String[] getForAdmin() {
        return new String[]{"/**"};
    }

    @Override
    public String[] getForUser() {
        return new String[0];
    }
}

package ru.krey.games.playerservice.utils;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.krey.libs.securitylib.interfaces.PathUtils;

@Component
public class PathUtilsImpl implements PathUtils {

    private static final String[] FOR_AUTHORIZED = new String[]{
            "/api/**"
    };

    @Override
    public @NonNull String[] getPublic() {
        return new String[0];
    }

    @Override
    public @NonNull String[] getForAuthorized() {
        return FOR_AUTHORIZED;
    }

    @Override
    public @NonNull String[] getForAdmin() {
        return new String[]{"/**"};
    }

    @Override
    public @NonNull String[] getForUser() {
        return new String[0];
    }
}

package ru.krey.games.fileservice.utils;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.krey.libs.securitylib.interfaces.PathUtils;

@Component
public class PathUtilsImpl implements PathUtils {
    @Override
    public @NonNull String[] getPublic() {
        return new String[0];
    }

    @Override
    public @NonNull String[] getForAuthorized() {
        return new String[0];
    }

    @Override
    public @NonNull String[] getForAdmin() {
        return new String[0];
    }

    @Override
    public @NonNull String[] getForUser() {
        return new String[0];
    }
}

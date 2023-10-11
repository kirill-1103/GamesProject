package ru.krey.libs.securitylib.interfaces;


import lombok.NonNull;

public interface PathUtils {
    @NonNull String[] getPublic();
    @NonNull String[] getForAuthorized();
    @NonNull String[] getForAdmin();
    @NonNull String[] getForUser();
}

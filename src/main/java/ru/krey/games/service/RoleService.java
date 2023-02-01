package ru.krey.games.service;

public  class RoleService {
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    public static String getRoleForConfig(String role){
        if(role.contains("ROLE_")){
            return role.substring(5);
        }
        return role;
    }
}

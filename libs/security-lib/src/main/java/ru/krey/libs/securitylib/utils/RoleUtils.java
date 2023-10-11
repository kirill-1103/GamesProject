package ru.krey.libs.securitylib.utils;

public  class RoleUtils {
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    public static String getRoleForConfig(String role){
        if(role.contains("ROLE_")){
            return role.substring(5);
        }
        return role;
    }
}

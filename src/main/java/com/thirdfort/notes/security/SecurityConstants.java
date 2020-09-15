package com.thirdfort.notes.security;

public class SecurityConstants {

    public static final String SIGN_UP_URL = "/users";
    public static final long EXPIRATION_TIME = 864000000;

    public static final String TOKEN_PREFIX = "Bearer";

    public static final String HEADER_STRING = "Authentication";

    public static String getTokenSecret() {
        return null;
    }
}

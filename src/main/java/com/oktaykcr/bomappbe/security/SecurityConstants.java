package com.oktaykcr.bomappbe.security;

public class SecurityConstants {
    public static final String SIGN_UP_URL = "/user/register";
    public static final String SIGN_IN_URL = "/user/login";
    public static final String VALIDATE_USER_URL = "/user/validate";
    public static final String SECRET = "oktaykcrsec";
    public static final long EXPIRATION_TIME = 432_000_000; // 5 gün
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_TOKEN = "Authorization";
    public static final String HEADER_USERNAME = "Username";
}

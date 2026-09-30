package com.example.spotiyou.TOKEN;

public class Token {
    private static String AccessToken;

    public Token() {
    }

    public static String getAccessToken() {
        return AccessToken;
    }

    public void limpiarToken() {
        AccessToken = null;
    }

    public static void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }
}

package com.example.spotiyou.APISpotify.Users.SeguirArtistas;

import android.util.Log;

import com.example.spotiyou.TOKEN.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SeguirArtista {

    private String id;

    public SeguirArtista(String id) {
        this.id = id;
    }
    private static Token token = new Token();
    private String accessToken = token.getAccessToken();

    public String llamarAPI() {
        try {
            String encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString());
            String urlString = "https://api.spotify.com/v1/me/following?type=artist&ids=" + encodedId;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                return response.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean comprobarApi(HttpURLConnection connection) throws IOException {
        int codigoRespuesta = connection.getResponseCode();
        return codigoRespuesta == HttpURLConnection.HTTP_OK;
    }

}

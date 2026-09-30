package com.example.spotiyou.APISpotify.Users;

import com.example.spotiyou.TOKEN.Token;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PerfilUsuario {

    private static final String BASE_URL = "https://api.spotify.com/v1/me";
    private static Token token = new Token();
    private String accessToken = token.getAccessToken();

    public String llamarAPI() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        if (comprobarApi(connection)) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            return response.toString();
        } else {
            return null;
        }
    }

    public boolean comprobarApi(HttpURLConnection connection) throws IOException {
        int codigoRespuesta = connection.getResponseCode();
        return codigoRespuesta == HttpURLConnection.HTTP_OK;
    }

    //-------------------METODO----------------------//

    public String nombreUsuario(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            String nombreUsuario = jsonObject.get("display_name").getAsString();
            return nombreUsuario;
        }
        return null;
    }

    public String idUsuario(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            String idUsuario = jsonObject.get("id").getAsString();
            return idUsuario;
        }
        return null;
    }

    public String imagenUsuario(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            JsonElement imagesElement = jsonObject.get("images");
            String imagen;
            if (imagesElement.getAsJsonArray().get(1).getAsJsonObject().get("url").getAsString() != null) {
                imagen = imagesElement.getAsJsonArray().get(1).getAsJsonObject().get("url").getAsString();
            }else if (imagesElement.getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString() != null){
                imagen = imagesElement.getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
            }else {
                imagen = null;
            }
            return imagen;
        }
        return null;
    }

    public Boolean esPremium(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            String productType = jsonObject.get("product").getAsString();

            return "premium".equals(productType);
        }
        return null;
    }

}

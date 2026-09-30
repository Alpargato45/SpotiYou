package com.example.spotiyou.APISpotify.Player;

import com.example.spotiyou.TOKEN.Token;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DevicesActivos {

    private static final String BASE_URL = "https://api.spotify.com/v1/me/player/devices";
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

    public String dispositivoEscuchandoAhora(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            JsonArray devicesArray = jsonObject.getAsJsonArray("devices");
            JsonObject deviceObject = devicesArray.get(0).getAsJsonObject();
            String device = deviceObject.get("name").getAsString();
            return device;
        }
        return null;
    }
}

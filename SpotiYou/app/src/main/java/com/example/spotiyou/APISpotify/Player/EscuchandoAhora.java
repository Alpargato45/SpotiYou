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

public class EscuchandoAhora {
    private static final String BASE_URL = "https://api.spotify.com/v1/me/player/currently-playing";
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

    public String urlImgEscuchandoAhora(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            JsonObject itemObject = jsonObject.getAsJsonObject("item");
            JsonObject albumObject = itemObject.getAsJsonObject("album");
            JsonArray imagesArray = albumObject.getAsJsonArray("images");
            JsonObject firstImage = imagesArray.get(0).getAsJsonObject();

            // Obtengo la URL de la imagen
            String imageUrl = firstImage.get("url").getAsString();
            return imageUrl;
        }
        return null;
    }

    public String cancionEscuchandoAhora(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            JsonObject itemObject = jsonObject.getAsJsonObject("item");
            String nombreCancion = itemObject.get("name").getAsString();
            return nombreCancion;
        }
        return null;
    }

    public String cantanteEscuchandoAhora(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();
            String artistas = "";

            JsonObject itemObject = jsonObject.getAsJsonObject("item");
            JsonArray artistsArray = itemObject.getAsJsonArray("artists");
            for (int i = 0; i < artistsArray.size(); i++) {
                JsonObject artistObject = artistsArray.get(i).getAsJsonObject();
                String artistName = artistObject.get("name").getAsString();
                artistas = artistas + artistName.replaceAll("\"","");
                if (i < artistsArray.size()-1) {
                    artistas = artistas + (", ");
                }
            }

            return artistas;
                }
        return null;
    }
}

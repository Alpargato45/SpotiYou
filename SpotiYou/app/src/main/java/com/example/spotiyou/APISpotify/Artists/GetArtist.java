package com.example.spotiyou.APISpotify.Artists;

import com.example.spotiyou.TOKEN.Token;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GetArtist {

    private String id;

    public GetArtist(String id) throws UnsupportedEncodingException {
        this.id = id;
    }

    private String BASE_URL = "https://api.spotify.com/v1/artists/";
    private static Token token = new Token();
    private String accessToken = token.getAccessToken();

    public String llamarAPI() throws IOException {
        String encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString());
        URL url = new URL(BASE_URL + encodedId);
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

    public String urlImagenArtista(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            JsonArray imagenesArray = jsonObject.getAsJsonArray("images");
            JsonObject primeraImagen = imagenesArray.get(0).getAsJsonObject();
            String imagen = primeraImagen.get("url").getAsString();
            return imagen;
        }
        return null;
    }

    public String nombreArtista(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            String nombreArtista = jsonObject.get("name").getAsString();
            return nombreArtista;
        }
        return null;
    }

    public String popularidadArtista(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            String popularidad = jsonObject.get("popularity").getAsString();
            return popularidad;
        }
        return null;
    }

    public String urlSpotify(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            String url = jsonObject.get("uri").getAsString();
            return url;
        }
        return null;
    }

    public String seguidoresArtista(String apiResponse) {
        if (apiResponse != null) {
            JsonObject jsonObject = JsonParser.parseString(apiResponse).getAsJsonObject();
            JsonObject followersObject = jsonObject.getAsJsonObject("followers");
            return followersObject.get("total").getAsString();
        }
        return null;
    }
}

package com.example.spotiyou.APISpotify.Artists;

import android.util.Log;

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
import java.util.ArrayList;

public class GetArtistAlbums {

    private String id;

    public GetArtistAlbums(String id) throws UnsupportedEncodingException {
        this.id = id;
    }

    private String BASE_URL = "https://api.spotify.com/v1/artists/";
    private static Token token = new Token();
    private String accessToken = token.getAccessToken();

    public String llamarAPI() throws IOException {
        String encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString());
        URL url = new URL(BASE_URL + encodedId + "/albums?include_groups=album");
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

    public ArrayList<String> nombreAlbumsArtista(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombreAlbumes = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                String albumName = item.get("name").getAsString();
                nombreAlbumes.add(i + 1 + ". " + albumName);
            }
            return nombreAlbumes;
        }
        return null;
    }

    public ArrayList<String> urlImagenesAlbumes(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> urlsImagenes = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                JsonArray imagesArray = item.getAsJsonArray("images");
                JsonObject firstImage = imagesArray.get(0).getAsJsonObject();
                String imageUrl = firstImage.get("url").getAsString();
                urlsImagenes.add(imageUrl);
            }
            return urlsImagenes;
        }
        return null;
    }

    public ArrayList<String> nombreArtistasAlbum(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombreArtistas = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                JsonArray artistsArray = item.getAsJsonArray("artists");
                StringBuilder artistas = new StringBuilder();
                for (int j = 0; j < artistsArray.size(); j++) {
                    JsonObject artistObject = artistsArray.get(j).getAsJsonObject();
                    String artistName = artistObject.get("name").getAsString();
                    artistas.append(artistName);
                    if (j < artistsArray.size() - 1) {
                        artistas.append(", ");
                    }
                }
                nombreArtistas.add(artistas.toString());
            }
            return nombreArtistas;
        }
        return null;
    }

    public ArrayList<String> obtenerIdsAlbums(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> idsAlbums = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                String albumId = item.get("id").getAsString();
                idsAlbums.add(albumId);
            }
            return idsAlbums;
        }
        return null;
    }
}

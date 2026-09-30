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

public class ArtistTopTracks {

    private String id;

    public ArtistTopTracks(String id) throws UnsupportedEncodingException {
        this.id = id;
    }

    private String BASE_URL = "https://api.spotify.com/v1/artists/";
    private static Token token = new Token();
    private String accessToken = token.getAccessToken();

    public String llamarAPI() throws IOException {
        String encodedId = URLEncoder.encode(id, StandardCharsets.UTF_8.toString());
        URL url = new URL(BASE_URL + encodedId+"/top-tracks");
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

    public ArrayList<String> obtenerURLPortadas(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> urlsPortadas = new ArrayList<>();

            JsonArray tracksArray = jsonObject.getAsJsonArray("tracks");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject track = tracksArray.get(i).getAsJsonObject();
                JsonObject albumObject = track.getAsJsonObject("album");
                JsonArray imagesArray = albumObject.getAsJsonArray("images");
                JsonObject firstImage = imagesArray.get(0).getAsJsonObject();
                String imageUrl = firstImage.get("url").getAsString();
                urlsPortadas.add(imageUrl);
            }
            return urlsPortadas;
        }
        return null;
    }

    public ArrayList<String> obtenerNombresCanciones(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombresCanciones = new ArrayList<>();

            JsonArray tracksArray = jsonObject.getAsJsonArray("tracks");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject track = tracksArray.get(i).getAsJsonObject();
                String nombreCancion = track.get("name").getAsString();
                nombresCanciones.add(nombreCancion);
            }
            return nombresCanciones;
        }
        return null;
    }

    public ArrayList<String> nombreCantanteMasEscuchados(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombreArtistas = new ArrayList<>();

            JsonArray tracksArray = jsonObject.getAsJsonArray("tracks");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject trackObject = tracksArray.get(i).getAsJsonObject();
                JsonArray artistsArray = trackObject.getAsJsonObject("album").getAsJsonArray("artists");
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

    public ArrayList<String> obtenerIdsCanciones(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> idsCanciones = new ArrayList<>();

            JsonArray tracksArray = jsonObject.getAsJsonArray("tracks");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject trackObject = tracksArray.get(i).getAsJsonObject();
                String trackId = trackObject.get("id").getAsString();
                idsCanciones.add(trackId);
            }
            return idsCanciones;
        }
        return null;
    }
}

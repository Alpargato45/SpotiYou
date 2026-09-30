package com.example.spotiyou.APISpotify.Search;

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
import java.util.ArrayList;

public class BuscarCancion {

    private String cancion;

    public BuscarCancion(String cancion) {
        this.cancion = cancion;
    }

    private static final String BASE_URL = "https://api.spotify.com/v1/search?q=";
    private static Token token = new Token();
    private String accessToken = token.getAccessToken();

    public String llamarAPI() throws IOException {
        URL url = new URL(BASE_URL + cancion + "&type=track&limit=10");
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


    public ArrayList<String> obtenerIdsCanciones(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> idsCanciones = new ArrayList<>();

            JsonObject tracksObject = jsonObject.getAsJsonObject("tracks");
            JsonArray tracksArray = tracksObject.getAsJsonArray("items");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject trackObject = tracksArray.get(i).getAsJsonObject();
                String trackId = trackObject.get("id").getAsString();
                idsCanciones.add(trackId);
            }
            return idsCanciones;
        }
        return null;
    }

    public ArrayList<String> obtenerNombresCanciones(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombresCanciones = new ArrayList<>();

            JsonObject tracksObject = jsonObject.getAsJsonObject("tracks");
            JsonArray tracksArray = tracksObject.getAsJsonArray("items");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject trackObject = tracksArray.get(i).getAsJsonObject();
                String trackName = trackObject.get("name").getAsString();
                nombresCanciones.add(trackName);
            }
            return nombresCanciones;
        }
        return null;
    }

    public ArrayList<String> obtenerNombreArtistas(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombreArtistas = new ArrayList<>();

            JsonObject tracksObject = jsonObject.getAsJsonObject("tracks");
            JsonArray tracksArray = tracksObject.getAsJsonArray("items");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject trackObject = tracksArray.get(i).getAsJsonObject();
                JsonArray artistsArray = trackObject.getAsJsonArray("artists");
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

    public ArrayList<String> obtenerUrlsFotosCanciones(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> urlsFotos = new ArrayList<>();

            JsonObject tracksObject = jsonObject.getAsJsonObject("tracks");
            JsonArray tracksArray = tracksObject.getAsJsonArray("items");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject trackObject = tracksArray.get(i).getAsJsonObject();
                JsonObject albumObject = trackObject.getAsJsonObject("album");
                JsonArray imagesArray = albumObject.getAsJsonArray("images");
                if (imagesArray.size() > 0) {
                    JsonObject imageObject = imagesArray.get(0).getAsJsonObject();
                    String imageUrl = imageObject.get("url").getAsString();
                    urlsFotos.add(imageUrl);
                }
            }
            return urlsFotos;
        }
        return null;
    }
}

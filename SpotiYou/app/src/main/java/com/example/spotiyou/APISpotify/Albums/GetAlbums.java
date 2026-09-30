package com.example.spotiyou.APISpotify.Albums;

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

public class GetAlbums {

    private String id;

    public GetAlbums(String id) throws UnsupportedEncodingException {
        this.id = id;
    }

    private String BASE_URL = "https://api.spotify.com/v1/albums/";
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

    public String urlImagenAlbum(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

                JsonArray imagenesArray = jsonObject.getAsJsonArray("images");
                if (imagenesArray.size() > 0) {
                    JsonObject primeraImagen = imagenesArray.get(0).getAsJsonObject();
                    String imagen = primeraImagen.get("url").getAsString();
                    return imagen;
                }
        }
        return null;
    }

    public String obtenerTituloAlbum(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();
                String titulo = jsonObject.get("name").getAsString();
                return titulo;
        }
        return null;
    }

    public String nombreArtistas(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            StringBuilder artistNames = new StringBuilder();

            JsonArray artistsArray = jsonObject.getAsJsonArray("artists");
            for (int i = 0; i < artistsArray.size(); i++) {
                JsonObject artistObject = artistsArray.get(i).getAsJsonObject();
                String artistName = artistObject.get("name").getAsString();
                artistNames.append(artistName);

                if (i < artistsArray.size() - 1) {
                    artistNames.append(", ");
                }
            }

            return artistNames.toString();
        }
        return null;
    }

    public String fechaSalidaAlbum(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            String releaseDate = jsonObject.get("release_date").getAsString();
            return releaseDate;
        }
        return null;
    }

    public int totalTracks(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            if (jsonObject.has("total_tracks")) {
                return jsonObject.get("total_tracks").getAsInt();
            }
        }
        return 0;
    }

    public ArrayList<String> obtenerNombresCanciones(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombresCanciones = new ArrayList<>();

            JsonObject tracksObject = jsonObject.getAsJsonObject("tracks");
            JsonArray tracksArray = tracksObject.getAsJsonArray("items");
            for (int i = 0; i < tracksArray.size(); i++) {
                JsonObject track = tracksArray.get(i).getAsJsonObject();
                String nombreCancion = track.get("name").getAsString();
                nombresCanciones.add(nombreCancion);
            }
            return nombresCanciones;
        }
        return null;
    }

    public ArrayList<String> nombreCantanteAlbum(String apiResponse) {
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
}

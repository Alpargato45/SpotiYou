package com.example.spotiyou.APISpotify.Tracks;

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

public class GetTrack {

    private String id;

    public GetTrack(String id) throws UnsupportedEncodingException {
        this.id = id;
    }

    private String BASE_URL = "https://api.spotify.com/v1/tracks/";
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

    public String portadaCancion(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            JsonObject albumObject = jsonObject.getAsJsonObject("album");
            JsonArray imagenesArray = albumObject.getAsJsonArray("images");
            JsonObject primeraImagen = imagenesArray.get(0).getAsJsonObject();
            String imagen = primeraImagen.get("url").getAsString();
            return imagen;
        }
        return null;
    }

    public String nombreCancion(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            String nombreCancion = jsonObject.get("name").getAsString();
            return nombreCancion;
        }
        return null;
    }

    public String popularidadCancion(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            String popularidad = jsonObject.get("popularity").getAsString();
            return popularidad;
        }
        return null;
    }

    public String urlPreviewCancion(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            String previewUrl = jsonObject.get("preview_url").getAsString();
            return previewUrl;
        }
        return null;
    }

    public String escucharEnSpotify(String apiResponse) {
        if (apiResponse != null) {
            JsonObject jsonObject = JsonParser.parseString(apiResponse).getAsJsonObject();
            JsonObject externalUrls = jsonObject.getAsJsonObject("external_urls");
            return externalUrls.get("spotify").getAsString();
        }
        return null;
    }

    public String fechaDeSalida(String apiResponse) {
        if (apiResponse != null) {
            JsonObject jsonObject = JsonParser.parseString(apiResponse).getAsJsonObject();
            JsonObject albumObject = jsonObject.getAsJsonObject("album");
            return albumObject.get("release_date").getAsString();
        }
        return null;
    }

    public static String nombreArtistas(String apiResponse) {
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

    public String duracionCancion(String apiResponse) {
        if (apiResponse != null) {
            JsonElement elemento = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = elemento.getAsJsonObject();

            Long tiempoMs = Long.valueOf(jsonObject.get("duration_ms").getAsString());
            long segundos = tiempoMs / 1000;
            long minutos = segundos / 60;
            segundos = segundos % 60;

            return minutos + ":" + String.format("%02d", segundos);
        }
        return null;
    }
        public String idPrimerArtista(String apiResponse) {
            if (apiResponse != null) {
                JsonElement elemento = JsonParser.parseString(apiResponse);
                JsonObject jsonObject = elemento.getAsJsonObject();

                // Verificar si el JSON contiene una lista de artistas
                if (jsonObject.has("artists")) {
                    JsonArray artistsArray = jsonObject.getAsJsonArray("artists");
                    if (artistsArray.size() > 0) {
                        JsonObject firstArtist = artistsArray.get(0).getAsJsonObject();
                        String artistId = firstArtist.get("id").getAsString();
                        return artistId;
                    }
                }
            }
            return null;
        }
}

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
import java.util.ArrayList;

public class EscuchadoRecientemente {

    private static final String BASE_URL = "https://api.spotify.com/v1/me/player/recently-played?limit=50";
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

    public ArrayList<String> urlImgEscuchadoReciente(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> urlsImagenes = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0;i<itemsArray.size();i++) {
                JsonObject firstItem = itemsArray.get(i).getAsJsonObject();
                JsonObject trackObject = firstItem.getAsJsonObject("track");
                JsonObject albumObject = trackObject.getAsJsonObject("album");
                JsonArray imagesArray = albumObject.getAsJsonArray("images");
                JsonObject firstImage = imagesArray.get(0).getAsJsonObject();
                String imageUrl = firstImage.get("url").getAsString();
                urlsImagenes.add(imageUrl);
            }
            return urlsImagenes;
        }
        return null;
    }

    public ArrayList<String> nombreCancionEscuchadoReciente(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombreCanciones = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0;i<itemsArray.size();i++) {
                JsonObject firstItem = itemsArray.get(i).getAsJsonObject();
                JsonObject trackObject = firstItem.getAsJsonObject("track");
                String trackName = String.valueOf(trackObject.get("name"));
                nombreCanciones.add(trackName.replaceAll("\"",""));
            }
            return nombreCanciones;
        }
        return null;
    }

    public ArrayList<String> nombreCantanteEscuchadoReciente(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombreArtistas = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0;i<itemsArray.size();i++) {
                String artistas = "";
                JsonObject firstItem = itemsArray.get(i).getAsJsonObject();
                JsonObject trackObject = firstItem.getAsJsonObject("track");
                JsonArray artistsArray = trackObject.getAsJsonArray("artists");
                for (int j = 0; j < artistsArray.size(); j++) {
                    JsonObject firstArtist = artistsArray.get(j).getAsJsonObject();
                    String artistName = String.valueOf(firstArtist.get("name"));
                    artistas = artistas + artistName.replaceAll("\"","");
                    if (j < artistsArray.size()-1) {
                        artistas = artistas + (", ");
                    }
                }
                    nombreArtistas.add(artistas);
            }
            return nombreArtistas;
        }
        return null;
    }

    public ArrayList<String> idCancionMasEscuchados(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> idCanciones = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                JsonObject trackObject = item.getAsJsonObject("track");
                String trackID = trackObject.get("id").getAsString();
                idCanciones.add(trackID);
            }
            return idCanciones;
        }
        return null;
    }
}

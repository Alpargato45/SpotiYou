package com.example.spotiyou.APISpotify.Users.Canciones;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public abstract class CancionesMasEscuchadas {

    public ArrayList<String> urlImgMasEscuchados(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> urlsImagenes = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                JsonObject albumObject = item.getAsJsonObject("album");
                JsonArray imagesArray = albumObject.getAsJsonArray("images");
                JsonObject firstImage = imagesArray.get(0).getAsJsonObject();
                String imageUrl = firstImage.get("url").getAsString();
                urlsImagenes.add(imageUrl);
            }
            return urlsImagenes;
        }
        return null;
    }

    public ArrayList<String> nombreCancionMasEscuchados(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombreCanciones = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                String trackName = item.get("name").getAsString();
                nombreCanciones.add(i+1 + ". " + trackName);
            }
            return nombreCanciones;
        }
        return null;
    }

    public ArrayList<String> nombreCantanteMasEscuchados(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombreArtistas = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                JsonObject trackObject = item.getAsJsonObject("album");
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
    
    public ArrayList<String> idCancionMasEscuchados(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> idCanciones = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                String itemID = item.get("id").getAsString();
                idCanciones.add(itemID);
            }
            return idCanciones;
        }
        return null;
    }
}

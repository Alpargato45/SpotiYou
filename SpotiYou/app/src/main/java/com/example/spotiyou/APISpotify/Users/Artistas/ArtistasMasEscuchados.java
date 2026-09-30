package com.example.spotiyou.APISpotify.Users.Artistas;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public abstract class ArtistasMasEscuchados {

    public ArrayList<String> urlImagenesArtistasMasEscuchados(String apiResponse) {
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

    public ArrayList<String> nombreArtistasMasEscuchados(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> nombresArtistas = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                String artistName = item.get("name").getAsString();
                nombresArtistas.add(i+1 + ". " + artistName);
            }
            return nombresArtistas;
        }
        return null;
    }

    public ArrayList<String> idArtistasMasEscuchados(String apiResponse) {
        if (apiResponse != null) {
            JsonElement element = JsonParser.parseString(apiResponse);
            JsonObject jsonObject = element.getAsJsonObject();

            ArrayList<String> idArtistas = new ArrayList<>();

            JsonArray itemsArray = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < itemsArray.size(); i++) {
                JsonObject item = itemsArray.get(i).getAsJsonObject();
                String idArtist = item.get("id").getAsString();
                idArtistas.add(idArtist);
            }
            return idArtistas;
        }
        return null;
    }
}

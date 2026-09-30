package com.example.spotiyou.APISpotify.Users.Artistas;

import com.example.spotiyou.TOKEN.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TopArtistas4Semanas extends ArtistasMasEscuchados {

    private static final String BASE_URL = "https://api.spotify.com/v1/me/top/artists?time_range=short_term&limit=50&offset=0";
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


    @Override
    public ArrayList<String> urlImagenesArtistasMasEscuchados(String apiResponse) {
        return super.urlImagenesArtistasMasEscuchados(apiResponse);
    }

    @Override
    public ArrayList<String> nombreArtistasMasEscuchados(String apiResponse) {
        return super.nombreArtistasMasEscuchados(apiResponse);
    }

    @Override
    public ArrayList<String> idArtistasMasEscuchados(String apiResponse) {
        return super.idArtistasMasEscuchados(apiResponse);
    }
}

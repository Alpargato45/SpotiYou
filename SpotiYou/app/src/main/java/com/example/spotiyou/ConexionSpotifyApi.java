package com.example.spotiyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.spotiyou.BBDD.BBDD;
import com.example.spotiyou.TOKEN.Codigo;
import com.example.spotiyou.TOKEN.Token;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;

import okhttp3.*;

public class ConexionSpotifyApi extends AppCompatActivity {

    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final String REDIRECT_URI = "spotiyou://callback";
    private static final String AUTHORIZATION_URL = "https://accounts.spotify.com/authorize" +
            "?client_id=" + CLIENT_ID +
            "&response_type=code" +
            "&redirect_uri=" + REDIRECT_URI +
            "&scope=user-read-private%20user-read-email%20user-read-recently-played%20user-read-currently-playing" +
            "%20user-read-playback-state%20user-top-read%20user-modify-playback-state%20user-follow-read%20user-follow-modify";

    private String authorizationCode;
    public WebView webView;
    private BBDD bbdd;
    private SQLiteDatabase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_spotify_api);

        // Inicializar la base de datos
        bbdd = new BBDD(this, "usuarioBBDD", null, 1);
        db = bbdd.getWritableDatabase();

        // Recibir la variable del Intent
        int tokenStatus = getIntent().getIntExtra("estado", 0);
        Codigo codigo = new Codigo();
        userId = codigo.getCodigo();
        Log.i("PRUEBAACTIVITYSPOTIFY", String.valueOf(userId));

        // Configuración de WebView y política de hilos
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("spotiyou");
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith(REDIRECT_URI)) {
                    handleRedirectUrl(url);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        // Verificar el estado del token
        if (tokenStatus == 0) {
            //Usuario nuevo, cargar la URL de autorización
            webView.loadUrl(AUTHORIZATION_URL);
        } else {
            // Usuario existente, renovar el token
            renewToken();
        }
    }

    private void handleRedirectUrl(String url) {
        // Extraer código de autorización
        URI uri = URI.create(url);
        String query = uri.getQuery();
        String[] queryParams = query.split("&");
        for (String param : queryParams) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && keyValue[0].equals("code")) {
                authorizationCode = keyValue[1];
                break;
            }
        }

        // Conseguir el token en segundo plano
        new TokenExchangeTask().execute(authorizationCode);
    }

    private class TokenExchangeTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String authorizationCode = params[0];
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("grant_type", "authorization_code")
                        .add("code", authorizationCode)
                        .add("redirect_uri", REDIRECT_URI)
                        .add("client_id", CLIENT_ID)
                        .add("client_secret", CLIENT_SECRET)
                        .build();

                Request request = new Request.Builder()
                        .url("https://accounts.spotify.com/api/token")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();

                String responseBody = response.body().string();
                Log.i("TOKEN_RESPONSE", responseBody); // Log de la respuesta
                return responseBody;
            } catch (IOException e) {
                Log.e("TOKEN_EXCHANGE_ERROR", "Error durante la solicitud del token", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String tokenResponse) {
            if (tokenResponse != null) {
                saveAccessTokenToDatabase(tokenResponse);

                String accessToken = parseAccessToken(tokenResponse);
                if (accessToken != null) {
                    Token token = new Token();
                    token.setAccessToken(accessToken);

                    Intent intent = new Intent(ConexionSpotifyApi.this, ActivityPrincipal.class);
                    intent.putExtra("codigo", userId);
                    ContentValues values = new ContentValues();
                    values.put("Estado", 1);
                    db.update("usuarioBBDD", values, "codigo = ?", new String[]{String.valueOf(userId)});
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.e("TOKEN_EXCHANGE_ERROR", "La respuesta del token es nula");
            }
        }
    }

    private void renewToken() {
        // Aquí puedes implementar la lógica para renovar el token
        new TokenRefreshTask().execute();
    }

    private class TokenRefreshTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                // Obtener el refresh token de la base de datos
                String refreshToken = getRefreshTokenFromDatabase();

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("grant_type", "refresh_token")
                        .add("refresh_token", refreshToken)
                        .add("client_id", CLIENT_ID)
                        .add("client_secret", CLIENT_SECRET)
                        .build();
                Headers headers = new Headers.Builder()
                        .add("Content-Type", "application/x-www-form-urlencoded")
                        .build();
                Request request = new Request.Builder()
                        .url("https://accounts.spotify.com/api/token")
                        .post(requestBody)
                        .headers(headers)
                        .build();

                Response response = client.newCall(request).execute();

                String responseBody = response.body().string();
                Log.i("TOKEN_REFRESH_RESPONSE", responseBody); // Log de la respuesta
                return responseBody;
            } catch (IOException e) {
                Log.e("TOKEN_REFRESH_ERROR", "Error durante la solicitud del token", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String tokenResponse) {
            if (tokenResponse != null) {
                saveAccessTokenToDatabase(tokenResponse);

                String accessToken = parseAccessToken(tokenResponse);
                if (accessToken != null) {
                    Token token = new Token();
                    token.setAccessToken(accessToken);

                    Intent intent = new Intent(ConexionSpotifyApi.this, ActivityPrincipal.class);
                    intent.putExtra("codigo", userId);
                    startActivity(intent);
                    finish();
                }
            } else {
                Log.e("TOKEN_REFRESH_ERROR", "La respuesta del token es nula");
            }
        }
    }

    private String getRefreshTokenFromDatabase() {
        // Consulta tu base de datos SQLite para obtener el refresh token
        db = bbdd.getReadableDatabase();
        Cursor cursor = db.query("usuarioBBDD", new String[]{"RefreshToken"}, "codigo = ?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String refreshToken = cursor.getString(cursor.getColumnIndexOrThrow("RefreshToken"));
            cursor.close();
            return refreshToken;
        }
        Log.e("DATABASE_ERROR", "No se encontró el refresh token en la base de datos");
        return null;
    }

    private void saveAccessTokenToDatabase(String tokenResponse) {
        try {
            // Extraer el access token y refresh token de la respuesta
            JsonParser parser = new JsonParser();
            JsonObject jsonResponse = parser.parse(tokenResponse).getAsJsonObject();

            String accessToken = jsonResponse.has("access_token") ? jsonResponse.get("access_token").getAsString() : null;
            String refreshToken = jsonResponse.has("refresh_token") ? jsonResponse.get("refresh_token").getAsString() : null;

            // Guardar el nuevo access token y refresh token en la base de datos SQLite
            db = bbdd.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Token", accessToken);
            if (refreshToken != null) {
                values.put("RefreshToken", refreshToken);
            }
            db.update("usuarioBBDD", values, "codigo = ?", new String[]{String.valueOf(userId)});
        } catch (Exception e) {
            Log.e("DATABASE_ERROR", "Error al guardar el token en la base de datos", e);
        }
    }

    private String parseAccessToken(String tokenResponse) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonResponse = parser.parse(tokenResponse).getAsJsonObject();

            return jsonResponse.has("access_token") ? jsonResponse.get("access_token").getAsString() : null;
        } catch (Exception e) {
            Log.e("TOKEN_ERROR", "Error al analizar el access token", e);
            return null;
        }
    }
}
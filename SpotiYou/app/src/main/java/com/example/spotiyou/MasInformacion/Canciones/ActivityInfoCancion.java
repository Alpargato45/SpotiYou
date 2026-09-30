package com.example.spotiyou.MasInformacion.Canciones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.spotiyou.APISpotify.Player.AñadirACola;
import com.example.spotiyou.APISpotify.Tracks.GetTrack;
import com.example.spotiyou.Fragments.MusicaFragment.CuatroSemanasFragment;
import com.example.spotiyou.MasInformacion.Artistas.ActivityInfoArtista;
import com.example.spotiyou.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ActivityInfoCancion extends AppCompatActivity {

    private Button btnAñadirCola;
    private Button btnEscucharEnSpotify;
    private ToggleButton btnPreviewCancion;
    private ImageView volverAtras;
    private ImageView portadaCancion;
    private TextView textoNombreCancion;
    private TextView textoPopularidadCancion;
    private TextView textoFechaSalida;
    private TextView textoArtistas;
    private TextView textoDuracionCancion;
    private String id;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.layout_cancion_info);
        btnAñadirCola = findViewById(R.id.btnAñadirCola);
        volverAtras = findViewById(R.id.volverAtras);
        portadaCancion = findViewById(R.id.imgCancion);
        textoNombreCancion = findViewById(R.id.textoNombreCancion);
        textoPopularidadCancion = findViewById(R.id.popularidadCancion);
        btnPreviewCancion = findViewById(R.id.btnEscucharAhora);
        btnEscucharEnSpotify = findViewById(R.id.btnEscucharEnSpotify);
        textoFechaSalida = findViewById(R.id.fechaSalidaCancion);
        textoArtistas = findViewById(R.id.textoArtistasCancion);
        textoDuracionCancion = findViewById(R.id.duracionCancion);

        Bundle args = getIntent().getExtras();
        id  = args.getString("id");

        rellenarDatos();

        btnAñadirCola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AñadirACola gestorCola = new AñadirACola(id);
                gestorCola.llamarAPI();
                Toast.makeText(ActivityInfoCancion.this, "Canción añadida a la Cola", Toast.LENGTH_SHORT).show();
            }
        });

        volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPreviewCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleButton button = (ToggleButton) v;
                if (button.isChecked()) {
                    GetTrack gestor = null;
                    try {
                        gestor = new GetTrack(id);
                        String apiResponse = gestor.llamarAPI();
                        String url = gestor.urlPreviewCancion(apiResponse);
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioAttributes(
                                new AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build()
                        );
                        mediaPlayer.setDataSource(url);
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mediaPlayer.start();
                            }
                        });
                        mediaPlayer.prepareAsync();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            }
        });

        btnEscucharEnSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTrack gestor = null;
                try {
                    gestor = new GetTrack(id);
                    String apiResponse = gestor.llamarAPI();
                    String url = gestor.escucharEnSpotify(apiResponse);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        textoArtistas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTrack gestor = null;
                try {
                    gestor = new GetTrack(id);
                    String apiResponse = gestor.llamarAPI();
                    String idArtista = gestor.idPrimerArtista(apiResponse);
                    Intent intent = new Intent(ActivityInfoCancion.this, ActivityInfoArtista.class);
                    intent.putExtra("id",idArtista);
                    startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void rellenarDatos() {
        GetTrack gestor = null;
        try {
            gestor = new GetTrack(id);
            String apiResponse = gestor.llamarAPI();
            String urlPortada = gestor.portadaCancion(apiResponse);
            Picasso.get().load(urlPortada).into(portadaCancion);
            String nombreCancion = gestor.nombreCancion(apiResponse);
            textoNombreCancion.setText(nombreCancion);
            String popularidadCancion = gestor.popularidadCancion(apiResponse);
            textoPopularidadCancion.append(popularidadCancion);
            String fechaSalida = gestor.fechaDeSalida(apiResponse);
            textoFechaSalida.append(fechaSalida);
            String artistas = gestor.nombreArtistas(apiResponse);
            textoArtistas.setText(artistas);
            String duracionCancion = gestor.duracionCancion(apiResponse);
            textoDuracionCancion.append(duracionCancion);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
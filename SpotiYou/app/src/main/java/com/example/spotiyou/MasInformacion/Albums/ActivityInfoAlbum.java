package com.example.spotiyou.MasInformacion.Albums;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotiyou.APISpotify.Albums.GetAlbums;
import com.example.spotiyou.APISpotify.Albums.GetAlbumsTracks;
import com.example.spotiyou.APISpotify.Artists.ArtistTopTracks;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.AdaptadorListViewCanciones;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.DatosCanciones;
import com.example.spotiyou.MasInformacion.Artistas.ActivityInfoArtista;
import com.example.spotiyou.MasInformacion.Canciones.ActivityInfoCancion;
import com.example.spotiyou.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ActivityInfoAlbum extends AppCompatActivity {

    private ImageView volverAtras;
    private ImageView portadaAlbum;
    private TextView txtTituloAlbum;
    private TextView txtNombreArtistas;
    private TextView txtFechaAlbum;
    private TextView txtnumTracks;
    private ListView listaCancionesAlbum;

    private String id;
    private GetAlbums gestor;
    private String apiResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_album_info);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        volverAtras = findViewById(R.id.volverAtras);
        portadaAlbum = findViewById(R.id.imgAlbum);
        txtTituloAlbum = findViewById(R.id.txtTituloAlbum);
        txtNombreArtistas = findViewById(R.id.txtArtistasAlbum);
        txtFechaAlbum = findViewById(R.id.fechaSalidaAlbum);
        txtnumTracks = findViewById(R.id.tracksAlbum);
        listaCancionesAlbum = findViewById(R.id.listaCancionesAlbum);

        Bundle args = getIntent().getExtras();
        id  = args.getString("id");

        try {
            gestor = new GetAlbums(id);
            apiResponse = gestor.llamarAPI();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        rellenarDatos();
        rellenarListaCancionesAlbum();

        volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void rellenarDatos() {

            String urlPortada = gestor.urlImagenAlbum(apiResponse);
            Picasso.get().load(urlPortada).into(portadaAlbum);

            String tituloAlbum = gestor.obtenerTituloAlbum(apiResponse);
            txtTituloAlbum.setText(tituloAlbum);

            String nombreArtist = gestor.nombreArtistas(apiResponse);
            txtNombreArtistas.setText(nombreArtist);

            String fecha = gestor.fechaSalidaAlbum(apiResponse);
            txtFechaAlbum.append(fecha);

            int tracks = gestor.totalTracks(apiResponse);
            txtnumTracks.append(tracks+"");
    }

    private void rellenarListaCancionesAlbum() {
        ArrayList<DatosCanciones> listaCompleta = new ArrayList<>();
        if (apiResponse != null) {
            ArrayList<String> listaCanciones = gestor.obtenerNombresCanciones(apiResponse);
            ArrayList<String> listaCantante = gestor.nombreCantanteAlbum(apiResponse);
            ArrayList<String> listaId = gestor.obtenerIdsCanciones(apiResponse);
            listaCompleta = juntarDatosCanciones(listaCanciones,listaCantante,listaId);
        }

        AdaptadorListViewCanciones adaptadorListViewCanciones = new AdaptadorListViewCanciones(listaCompleta, ActivityInfoAlbum.this);
        listaCancionesAlbum.setAdapter(adaptadorListViewCanciones);

        listaCancionesAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatosCanciones cancion = (DatosCanciones) listaCancionesAlbum.getItemAtPosition(position);
                String idCancion = cancion.getId();
                Intent intent = new Intent(ActivityInfoAlbum.this, ActivityInfoCancion.class);
                intent.putExtra("id",idCancion);
                startActivity(intent);
            }
        });
    }

    private ArrayList<DatosCanciones> juntarDatosCanciones(ArrayList<String> nombresCancion, ArrayList<String> nombresArtista, ArrayList<String> idCancion) {
        ArrayList<DatosCanciones> listaDatos = new ArrayList<>();
        DatosCanciones soporte;
        String url = gestor.urlImagenAlbum(apiResponse);
        if (nombresCancion.size()!=0) {
            for (int i = 0; i < nombresCancion.size(); i++) {
                soporte = new DatosCanciones(url,nombresCancion.get(i),nombresArtista.get(i),idCancion.get(i));
                listaDatos.add(soporte);
            }
        }else {
            Toast.makeText(this, "Ha habido un error al descargar tus canciones recientes", Toast.LENGTH_SHORT).show();
        }
        return listaDatos;
    }
}
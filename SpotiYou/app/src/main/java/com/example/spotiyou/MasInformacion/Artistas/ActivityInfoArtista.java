package com.example.spotiyou.MasInformacion.Artistas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.spotiyou.APISpotify.Artists.ArtistTopTracks;
import com.example.spotiyou.APISpotify.Artists.GetArtist;
import com.example.spotiyou.APISpotify.Artists.GetArtistAlbums;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.AdaptadorListViewCanciones;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.DatosCanciones;
import com.example.spotiyou.APISpotify.Users.SeguirArtistas.ComprobarSeguirArtista;
import com.example.spotiyou.APISpotify.Users.SeguirArtistas.DejarSeguirArtista;
import com.example.spotiyou.APISpotify.Users.SeguirArtistas.SeguirArtista;
import com.example.spotiyou.MasInformacion.Albums.ActivityInfoAlbum;
import com.example.spotiyou.MasInformacion.Canciones.ActivityInfoCancion;
import com.example.spotiyou.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ActivityInfoArtista extends AppCompatActivity {

    private ImageView volverAtras;
    private ImageView fotoArtista;
    private TextView textoNombreArtista;
    private TextView textoPopularidad;
    private TextView textoSeguidores;
    private TextView textoTopCanciones;
    private Button verEnSpotify;
    private ListView listaTopCanciones;
    private ListView listaAlbums;
    private TextView txtAlbum;
    private ToggleButton btnSeguirArtista;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        setContentView(R.layout.layout_artista_info);
        volverAtras = findViewById(R.id.volverAtras);
        fotoArtista = findViewById(R.id.imgArtista);
        textoNombreArtista = findViewById(R.id.txtNombreArtista);
        textoPopularidad = findViewById(R.id.txtPopularidadArtista);
        verEnSpotify = findViewById(R.id.btnEscucharEnSpotify);
        textoSeguidores = findViewById(R.id.txtSeguidoresArtista);
        textoTopCanciones = findViewById(R.id.txtTopCanciones);
        listaTopCanciones = findViewById(R.id.listaTopCanciones);
        listaAlbums = findViewById(R.id.listaAlbums);
        txtAlbum = findViewById(R.id.txtAlbums);
        btnSeguirArtista = findViewById(R.id.toggleSeguirArtista);


        Bundle args = getIntent().getExtras();
        id  = args.getString("id");

        rellenarDatos();

        volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        verEnSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    GetArtist gestor = new GetArtist(id);
                    String apiResponse = gestor.llamarAPI();
                    String url = gestor.urlSpotify(apiResponse);
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

        btnSeguirArtista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToggleButton button = (ToggleButton) v;
                if (button.isChecked()) {
                    SeguirArtista gestor = new SeguirArtista(id);
                    gestor.llamarAPI();
                }else {
                    DejarSeguirArtista gestor = new DejarSeguirArtista(id);
                    gestor.llamarAPI();
                }
            }
        });

    }

    private void botonSeguimientoArtista() throws IOException {
        ComprobarSeguirArtista gestor = new ComprobarSeguirArtista(id);
        String apiResponse = gestor.llamarAPI();
        boolean siguiendo = gestor.sigueAlArtista(apiResponse);
        btnSeguirArtista.setChecked(siguiendo);
    }

    private void rellenarDatos() {
        try {
            GetArtist gestor = new GetArtist(id);
            String apiResponse = gestor.llamarAPI();
            String urlPortada = gestor.urlImagenArtista(apiResponse);
            Picasso.get().load(urlPortada).into(fotoArtista);
            String nombreArtista = gestor.nombreArtista(apiResponse);
            textoNombreArtista.setText(nombreArtista);
            String popularidadArtista = gestor.popularidadArtista(apiResponse);
            textoPopularidad.append(popularidadArtista);
            String seguidoresArtista = gestor.seguidoresArtista(apiResponse);
            textoSeguidores.append(seguidoresArtista);

            rellenarListaTopCanciones();
            rellenarListaAlbums();
            botonSeguimientoArtista();

            textoTopCanciones.append(nombreArtista);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rellenarListaTopCanciones() throws IOException {
        ArtistTopTracks gestor = new ArtistTopTracks(id);
        String apiResponse = gestor.llamarAPI();
        ArrayList<DatosCanciones> listaCompleta = new ArrayList<>();
        if (apiResponse != null) {
            ArrayList<String> listaURL = gestor.obtenerURLPortadas(apiResponse);
            ArrayList<String> listaCanciones = gestor.obtenerNombresCanciones(apiResponse);
            ArrayList<String> listaCantante = gestor.nombreCantanteMasEscuchados(apiResponse);
            ArrayList<String> listaId = gestor.obtenerIdsCanciones(apiResponse);
            listaCompleta = juntarDatosCanciones(listaURL,listaCanciones,listaCantante,listaId);
        }

        AdaptadorListViewCanciones adaptadorListViewCanciones = new AdaptadorListViewCanciones(listaCompleta, ActivityInfoArtista.this);
        listaTopCanciones.setAdapter(adaptadorListViewCanciones);

        listaTopCanciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatosCanciones cancion = (DatosCanciones) listaTopCanciones.getItemAtPosition(position);
                String idCancion = cancion.getId();
                Intent intent = new Intent(ActivityInfoArtista.this, ActivityInfoCancion.class);
                intent.putExtra("id",idCancion);
                startActivity(intent);
            }
        });
    }

    private void rellenarListaAlbums() throws IOException {
        GetArtistAlbums gestor = new GetArtistAlbums(id);
        String apiResponse = gestor.llamarAPI();
        ArrayList<DatosCanciones> listaCompleta = new ArrayList<>();
        if (apiResponse != null) {
            txtAlbum.setVisibility(View.VISIBLE);
            ArrayList<String> listaURL = gestor.urlImagenesAlbumes(apiResponse);
            ArrayList<String> listaCanciones = gestor.nombreAlbumsArtista(apiResponse);
            ArrayList<String> listaCantante = gestor.nombreArtistasAlbum(apiResponse);
            ArrayList<String> listaId = gestor.obtenerIdsAlbums(apiResponse);
            listaCompleta = juntarDatosCanciones(listaURL,listaCanciones,listaCantante,listaId);
        }
        AdaptadorListViewCanciones adaptadorListViewCanciones = new AdaptadorListViewCanciones(listaCompleta, ActivityInfoArtista.this);
        listaAlbums.setAdapter(adaptadorListViewCanciones);

        listaAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatosCanciones album = (DatosCanciones) listaAlbums.getItemAtPosition(position);
                String idAlbum = album.getId();
                Intent intent = new Intent(ActivityInfoArtista.this, ActivityInfoAlbum.class);
                intent.putExtra("id",idAlbum);
                startActivity(intent);
            }
        });
    }

    private ArrayList<DatosCanciones> juntarDatosCanciones(ArrayList<String> urlImagenes, ArrayList<String> nombresCancion, ArrayList<String> nombresArtista, ArrayList<String> idCancion) {
        ArrayList<DatosCanciones> listaDatos = new ArrayList<>();
        DatosCanciones soporte;
        if (urlImagenes.size()!=0) {
            for (int i = 0; i < urlImagenes.size(); i++) {
                soporte = new DatosCanciones(urlImagenes.get(i),nombresCancion.get(i),nombresArtista.get(i),idCancion.get(i));
                listaDatos.add(soporte);
            }
        }else {
            //Toast.makeText(this, "Ha habido un error al descargar tus canciones recientes", Toast.LENGTH_SHORT).show();
        }
        return listaDatos;
    }
}
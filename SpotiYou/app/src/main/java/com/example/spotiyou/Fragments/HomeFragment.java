package com.example.spotiyou.Fragments;

import static android.content.Intent.getIntent;
import static android.content.Intent.getIntentOld;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotiyou.APISpotify.Personalizados.Artistas.DatosArtistas;
import com.example.spotiyou.APISpotify.Player.DevicesActivos;
import com.example.spotiyou.APISpotify.Player.EscuchadoRecientemente;
import com.example.spotiyou.APISpotify.Player.EscuchandoAhora;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.AdaptadorListViewCanciones;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.DatosCanciones;
import com.example.spotiyou.APISpotify.Users.PerfilUsuario;
import com.example.spotiyou.BBDD.BBDD;
import com.example.spotiyou.Fragments.MusicaFragment.CuatroSemanasFragment;
import com.example.spotiyou.MasInformacion.Canciones.ActivityInfoCancion;
import com.example.spotiyou.R;
import com.example.spotiyou.TOKEN.Codigo;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ImageView imagenEscuchandoAhora;
    private ListView listaEscuchasRecientes;
    private TextView deviceEscuchando;
    private TextView textoCancionAhora;
    private RelativeLayout layoutEscuchandoAhora;
    private TextView textoNombreUsuario;
    private TextView textoIdUsuario;
    private ImageView imagenUsuario;
    private TextView cantanteEscuchandoAhora;
    private ImageView imgCorona;
    private BBDD usuarioBBDD;
    private SQLiteDatabase db;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        imagenEscuchandoAhora = view.findViewById(R.id.imgEscuchandoAhora);
        listaEscuchasRecientes = view.findViewById(R.id.ListViewEscuchasRecientes);
        deviceEscuchando = view.findViewById(R.id.textoEscuchandoDevice);
        textoCancionAhora = view.findViewById(R.id.textoEscuchandoAhoraNombreCancion);
        cantanteEscuchandoAhora = view.findViewById(R.id.textoEscuchandoAhoraNombreCantante);
        layoutEscuchandoAhora = view.findViewById(R.id.layoutEscuchandoAhora);
        textoNombreUsuario = view.findViewById(R.id.textoNombreUsuario);
        textoIdUsuario = view.findViewById(R.id.textoIdUsuario);
        imagenUsuario = view.findViewById(R.id.imgUsuarioSpotify);
        imgCorona = view.findViewById(R.id.imagenCorona);

        usuarioBBDD = new BBDD(this.getContext(), "usuarioBBDD", null, 1);
        db = usuarioBBDD.getWritableDatabase();

        Codigo codigo = new Codigo();
        userId = codigo.getCodigo();
        Log.i("PRUEBAACTIVITYHOME", String.valueOf(userId));

        PerfilUsuario gestorUsuario = new PerfilUsuario();
        EscuchandoAhora gestorAhora = new EscuchandoAhora();
        EscuchadoRecientemente gestorReciente = new EscuchadoRecientemente();
        DevicesActivos gestorDevices = new DevicesActivos();

        try {
            gestionEscuchasAhora(gestorAhora,gestorDevices);
            gestionEscuchasRecientes(gestorReciente);
            gestionUsuario(gestorUsuario);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        listaEscuchasRecientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatosCanciones cancion = (DatosCanciones) listaEscuchasRecientes.getItemAtPosition(position);
                String idCancion = cancion.getId();
                Intent intent = new Intent(HomeFragment.this.getContext(), ActivityInfoCancion.class);
                intent.putExtra("id",idCancion);
                startActivity(intent);
            }
        });
        return view;
    }

    private void gestionUsuario(PerfilUsuario gestorUsuario) throws IOException {
        String apiResponseUsuario = gestorUsuario.llamarAPI();
        if (apiResponseUsuario!=null) {
            String nombre = gestorUsuario.nombreUsuario(apiResponseUsuario);
            String id = gestorUsuario.idUsuario(apiResponseUsuario);
            String imgUsuario = gestorUsuario.imagenUsuario(apiResponseUsuario);
            Boolean esPremium = gestorUsuario.esPremium(apiResponseUsuario);

            textoNombreUsuario.append(nombre);
            textoIdUsuario.setText(id);
            Picasso.get().load(imgUsuario).into(imagenUsuario);
            if (esPremium) {
                imgCorona.setVisibility(View.VISIBLE);
            }
            ContentValues values = new ContentValues();
            values.put("Nombre", nombre);
            values.put("Imagen", imgUsuario);

            db.update("usuarioBBDD", values, "codigo = ?", new String[]{String.valueOf(userId)});
        }
    }

    private void gestionEscuchasAhora(EscuchandoAhora gestorAhora,DevicesActivos gestorDevices) throws IOException {
        String apiResponseAhora = gestorAhora.llamarAPI();
        String apiResponseDevice = gestorDevices.llamarAPI();
        if (apiResponseAhora != null) {
            layoutEscuchandoAhora.setVisibility(View.VISIBLE);
            String url = gestorAhora.urlImgEscuchandoAhora(apiResponseAhora);
            String cancionAhora = gestorAhora.cancionEscuchandoAhora(apiResponseAhora);
            String device = gestorDevices.dispositivoEscuchandoAhora(apiResponseDevice);
            String cantante = gestorAhora.cantanteEscuchandoAhora(apiResponseAhora);

            if (url != null) {
                Picasso.get().load(url).into(imagenEscuchandoAhora);
            }
            textoCancionAhora.setText(cancionAhora);
            deviceEscuchando.append(device);
            cantanteEscuchandoAhora.setText(cantante);
        }else {
            layoutEscuchandoAhora.setVisibility(View.GONE);
        }
    }

    private void gestionEscuchasRecientes(EscuchadoRecientemente gestorReciente) throws IOException {
        String apiResponseReciente = gestorReciente.llamarAPI();
        ArrayList<DatosCanciones> listaCompleta = new ArrayList<>();
        if (apiResponseReciente!=null) {
            ArrayList<String> listaURL = gestorReciente.urlImgEscuchadoReciente(apiResponseReciente);
            ArrayList<String> listaCanciones = gestorReciente.nombreCancionEscuchadoReciente(apiResponseReciente);
            ArrayList<String> listaCantante = gestorReciente.nombreCantanteEscuchadoReciente(apiResponseReciente);
            ArrayList<String> listaId = gestorReciente.idCancionMasEscuchados(apiResponseReciente);
            listaCompleta = juntarDatosCanciones(listaURL,listaCanciones,listaCantante,listaId);
        }

        AdaptadorListViewCanciones miAdaptadorListViewCanciones = new AdaptadorListViewCanciones(listaCompleta,HomeFragment.this);
        listaEscuchasRecientes.setAdapter(miAdaptadorListViewCanciones);
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
            Toast.makeText(this.getContext(), "Ha habido un error al descargar tus canciones recientes", Toast.LENGTH_SHORT).show();
        }
        return listaDatos;
    }
}
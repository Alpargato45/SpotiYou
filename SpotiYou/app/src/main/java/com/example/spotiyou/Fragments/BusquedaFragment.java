package com.example.spotiyou.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spotiyou.APISpotify.Personalizados.Canciones.AdaptadorListViewCanciones;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.DatosCanciones;
import com.example.spotiyou.APISpotify.Search.BuscarCancion;
import com.example.spotiyou.MasInformacion.Canciones.ActivityInfoCancion;
import com.example.spotiyou.R;

import java.io.IOException;
import java.util.ArrayList;

public class BusquedaFragment extends Fragment {

    private EditText buscadorCanciones;
    private Button btnBuscarCanciones;
    private ListView listaCancionesBuscadas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);
        buscadorCanciones = view.findViewById(R.id.barraBusqueda);
        btnBuscarCanciones = view.findViewById(R.id.btnAceptarBusqueda);
        listaCancionesBuscadas = view.findViewById(R.id.listaCancionesBusqueda);

        btnBuscarCanciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cancion = String.valueOf(buscadorCanciones.getText());
                if (!cancion.isEmpty()) {
                    try {
                        añadirDatos(cancion);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(view.getContext(), "Primero tienes que escribir una canción", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listaCancionesBuscadas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatosCanciones cancion = (DatosCanciones) listaCancionesBuscadas.getItemAtPosition(position);
                String idCancion = cancion.getId();
                Intent intent = new Intent(BusquedaFragment.this.getContext(), ActivityInfoCancion.class);
                intent.putExtra("id",idCancion);
                startActivity(intent);
            }
        });


        return view;
    }

    private void añadirDatos(String cancion) throws IOException {
        BuscarCancion gestor = new BuscarCancion(cancion);
        String apiResponse = gestor.llamarAPI();
        ArrayList<DatosCanciones> listaCompleta = new ArrayList<>();
        if (apiResponse!=null) {
            ArrayList<String> listaURL = gestor.obtenerUrlsFotosCanciones(apiResponse);
            ArrayList<String> listaCanciones = gestor.obtenerNombresCanciones(apiResponse);
            ArrayList<String> listaCantante = gestor.obtenerNombreArtistas(apiResponse);
            ArrayList<String> listaId = gestor.obtenerIdsCanciones(apiResponse);
            listaCompleta = juntarDatosCanciones(listaURL,listaCanciones,listaCantante,listaId);
        }
        AdaptadorListViewCanciones miAdaptadorListViewCanciones = new AdaptadorListViewCanciones(listaCompleta,BusquedaFragment.this);
        listaCancionesBuscadas.setAdapter(miAdaptadorListViewCanciones);
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
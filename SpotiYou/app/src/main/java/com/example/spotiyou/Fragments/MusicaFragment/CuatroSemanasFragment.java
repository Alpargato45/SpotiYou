package com.example.spotiyou.Fragments.MusicaFragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.spotiyou.APISpotify.Personalizados.Artistas.AdaptadorGridViewArtistas;
import com.example.spotiyou.APISpotify.Personalizados.Artistas.AdaptadorListViewArtistas;
import com.example.spotiyou.APISpotify.Personalizados.Artistas.DatosArtistas;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.AdaptadorGridViewCanciones;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.AdaptadorListViewCanciones;
import com.example.spotiyou.APISpotify.Personalizados.Canciones.DatosCanciones;
import com.example.spotiyou.APISpotify.Users.Artistas.TopArtistas4Semanas;
import com.example.spotiyou.APISpotify.Users.Canciones.TopCanciones4Semanas;
import com.example.spotiyou.MasInformacion.Artistas.ActivityInfoArtista;
import com.example.spotiyou.MasInformacion.Canciones.ActivityInfoCancion;
import com.example.spotiyou.R;

import java.io.IOException;
import java.util.ArrayList;

public class CuatroSemanasFragment extends Fragment {

    private GridView gridView;
    private ListView listView;
    private ImageView imagenGrid;
    private ImageView imagenList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuatro_semanas, container, false);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        gridView = view.findViewById(R.id.gridView4Semanas);
        listView = view.findViewById(R.id.listView4Semanas);
        imagenGrid = getActivity().findViewById(R.id.imagenGrid);
        imagenList = getActivity().findViewById(R.id.imagenList);

        Bundle args = getArguments();
        if (args != null) {
            String parentActivityName = args.getString("parentActivityName");
            if (parentActivityName.equals("CancionesFragment")) {
                mostrarCanciones();
            } else if (parentActivityName.equals("ArtistasFragment")) {
                mostrarArtistas();
            }
        }

        //BOTONES GRID Y LIST

        imagenGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenGrid.setVisibility(View.INVISIBLE);
                imagenList.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        imagenList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenGrid.setVisibility(View.VISIBLE);
                imagenList.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (imagenGrid.getVisibility() == View.VISIBLE) {
            gridView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else if (imagenList.getVisibility() == View.VISIBLE) {
            gridView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

        imagenGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenGrid.setVisibility(View.INVISIBLE);
                imagenList.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        imagenList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagenGrid.setVisibility(View.VISIBLE);
                imagenList.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });
    }

    private void mostrarCanciones() {
        TopCanciones4Semanas gestorEscuchas = new TopCanciones4Semanas();
        try {
            String apiResponse = gestorEscuchas.llamarAPI();
            ArrayList<DatosCanciones> listaCompleta = new ArrayList<>();
            if (apiResponse != null) {
                ArrayList<String> listaURL = gestorEscuchas.urlImgMasEscuchados(apiResponse);
                ArrayList<String> listaCanciones = gestorEscuchas.nombreCancionMasEscuchados(apiResponse);
                ArrayList<String> listaCantante = gestorEscuchas.nombreCantanteMasEscuchados(apiResponse);
                ArrayList<String> listaId = gestorEscuchas.idCancionMasEscuchados(apiResponse);
                listaCompleta = juntarDatosCanciones(listaURL,listaCanciones,listaCantante,listaId);
            }

            AdaptadorGridViewCanciones adapter = new AdaptadorGridViewCanciones(listaCompleta,CuatroSemanasFragment.this);
            gridView.setAdapter(adapter);

            AdaptadorListViewCanciones adaptadorListViewCanciones = new AdaptadorListViewCanciones(listaCompleta,CuatroSemanasFragment.this);
            listView.setAdapter(adaptadorListViewCanciones);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DatosCanciones cancion = (DatosCanciones) gridView.getItemAtPosition(position);
                    String idCancion = cancion.getId();
                    Intent intent = new Intent(CuatroSemanasFragment.this.getContext(), ActivityInfoCancion.class);
                    intent.putExtra("id",idCancion);
                    startActivity(intent);
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DatosCanciones cancion = (DatosCanciones) listView.getItemAtPosition(position);
                    String idCancion = cancion.getId();
                    Intent intent = new Intent(CuatroSemanasFragment.this.getContext(), ActivityInfoCancion.class);
                    intent.putExtra("id",idCancion);
                    //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CuatroSemanasFragment.this.getActivity());
                    startActivity(intent);
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void mostrarArtistas() {
        TopArtistas4Semanas gestorEscuchas = new TopArtistas4Semanas();
        try {
            String apiResponse = gestorEscuchas.llamarAPI();
            ArrayList<DatosArtistas> listaCompleta = new ArrayList<>();
            if (apiResponse != null) {
                ArrayList<String> listaURL = gestorEscuchas.urlImagenesArtistasMasEscuchados(apiResponse);
                ArrayList<String> listaNombres = gestorEscuchas.nombreArtistasMasEscuchados(apiResponse);
                ArrayList<String> listaID = gestorEscuchas.idArtistasMasEscuchados(apiResponse);
                listaCompleta = juntarDatosArtistas(listaURL,listaNombres,listaID);
            }
            AdaptadorGridViewArtistas adapter = new AdaptadorGridViewArtistas(listaCompleta,CuatroSemanasFragment.this);
            gridView.setAdapter(adapter);

            AdaptadorListViewArtistas adaptadorListViewArtistas = new AdaptadorListViewArtistas(listaCompleta,CuatroSemanasFragment.this);
            listView.setAdapter(adaptadorListViewArtistas);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DatosArtistas artista = (DatosArtistas) gridView.getItemAtPosition(position);
                    String idArtista = artista.getId();
                    Intent intent = new Intent(CuatroSemanasFragment.this.getContext(), ActivityInfoArtista.class);
                    intent.putExtra("id",idArtista);
                    startActivity(intent);
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DatosArtistas artista = (DatosArtistas) gridView.getItemAtPosition(position);
                    String idArtista = artista.getId();
                    Intent intent = new Intent(CuatroSemanasFragment.this.getContext(), ActivityInfoArtista.class);
                    intent.putExtra("id",idArtista);
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private ArrayList<DatosArtistas> juntarDatosArtistas(ArrayList<String> urlImagenes, ArrayList<String> nombresArtistas,ArrayList<String> idArtistas) {
        ArrayList<DatosArtistas> listaDatos = new ArrayList<>();
        DatosArtistas soporte;
        if (urlImagenes.size()!=0) {
            for (int i = 0; i < urlImagenes.size(); i++) {
                soporte = new DatosArtistas(urlImagenes.get(i),nombresArtistas.get(i),idArtistas.get(i));
                listaDatos.add(soporte);
            }
        }else {
            Toast.makeText(this.getContext(), "Ha habido un error al descargar tus artistas recientes", Toast.LENGTH_SHORT).show();
        }
        return listaDatos;
    }
}
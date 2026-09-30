package com.example.spotiyou.APISpotify.Personalizados.Artistas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.spotiyou.APISpotify.Personalizados.Canciones.DatosCanciones;
import com.example.spotiyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorGridViewArtistas extends BaseAdapter {
    private ArrayList<DatosArtistas> datos;
    private Fragment fragment;

    public AdaptadorGridViewArtistas(ArrayList<DatosArtistas> datos, Fragment fragment) {
        this.datos = datos;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mostrado = LayoutInflater.from(fragment.getContext());
        View elemento = mostrado.inflate(R.layout.elemento_gridview_artistas, parent, false);

        ImageView portada = elemento.findViewById(R.id.imagenGridArtista);
        Picasso.get().load(datos.get(position).getImagen()).into(portada);

        TextView nombre = (TextView) elemento.findViewById(R.id.nombreGridCantante);
        nombre.setText(datos.get(position).getNombre());

        return elemento;
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

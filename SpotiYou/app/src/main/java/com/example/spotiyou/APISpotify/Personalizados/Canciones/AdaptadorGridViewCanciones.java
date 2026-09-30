package com.example.spotiyou.APISpotify.Personalizados.Canciones;

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

public class AdaptadorGridViewCanciones extends BaseAdapter {
    private ArrayList<DatosCanciones> datos;
    private Fragment fragment;

    public AdaptadorGridViewCanciones(ArrayList<DatosCanciones> datos, Fragment fragment) {
        this.datos = datos;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mostrado = LayoutInflater.from(fragment.getContext());
        View elemento = mostrado.inflate(R.layout.elemento_gridview_canciones, parent, false);

        ImageView portada = elemento.findViewById(R.id.imagenGridCancion);
        Picasso.get().load(datos.get(position).getImagen()).into(portada);

        TextView cancion = (TextView) elemento.findViewById(R.id.tituloGridCancion);
        cancion.setText(datos.get(position).getCancion());

        TextView cantante = (TextView) elemento.findViewById(R.id.cantanteGridCancion);
        cantante.setText(datos.get(position).getCantante());

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
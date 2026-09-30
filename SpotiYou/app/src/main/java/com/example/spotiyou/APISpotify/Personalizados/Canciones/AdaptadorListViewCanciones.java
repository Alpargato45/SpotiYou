package com.example.spotiyou.APISpotify.Personalizados.Canciones;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.spotiyou.MasInformacion.Albums.ActivityInfoAlbum;
import com.example.spotiyou.MasInformacion.Artistas.ActivityInfoArtista;
import com.example.spotiyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorListViewCanciones extends BaseAdapter {

    private ArrayList<DatosCanciones> datos;
    private Fragment fragment;
    private ActivityInfoArtista activityInfoArtista;
    private ActivityInfoAlbum activityInfoAlbum;

    public AdaptadorListViewCanciones(ArrayList<DatosCanciones> datos, Fragment fragment) {
        this.datos = datos;
        this.fragment = fragment;
    }

    public AdaptadorListViewCanciones(ArrayList<DatosCanciones> datos, ActivityInfoArtista activityInfoArtista) {
        this.datos = datos;
        this.activityInfoArtista = activityInfoArtista;
    }

    public AdaptadorListViewCanciones(ArrayList<DatosCanciones> datos, ActivityInfoAlbum activityInfoAlbum) {
        this.datos = datos;
        this.activityInfoAlbum = activityInfoAlbum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mostrado = null;
        if (fragment!=null) {
            mostrado = LayoutInflater.from(fragment.getContext());
        }else if (activityInfoArtista != null){
            mostrado = LayoutInflater.from(activityInfoArtista);
        }else if (activityInfoAlbum != null){
            mostrado = LayoutInflater.from(activityInfoAlbum);
        }
        View elemento = mostrado.inflate(R.layout.elemento_lista_canciones, parent, false);

        ImageView portada = elemento.findViewById(R.id.imagenListado);
        Picasso.get().load(datos.get(position).getImagen()).into(portada);

        TextView cancion = (TextView) elemento.findViewById(R.id.textoNombreCancionListado);
        cancion.setText(datos.get(position).getCancion());

        TextView cantante = (TextView) elemento.findViewById(R.id.textoNombreCantanteListado);
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

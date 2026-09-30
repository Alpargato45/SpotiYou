package com.example.spotiyou.BBDD;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.spotiyou.APISpotify.Personalizados.Artistas.DatosArtistas;
import com.example.spotiyou.ActivityElegirCuenta;
import com.example.spotiyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorListViewUsuarios extends BaseAdapter {

    private ArrayList<DatosUsuarios> datos;
    private ActivityElegirCuenta view;

    public AdaptadorListViewUsuarios(ArrayList<DatosUsuarios> datos, ActivityElegirCuenta view) {
        this.datos = datos;
        this.view = view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mostrado = LayoutInflater.from(view);
        View elemento = mostrado.inflate(R.layout.elemento_listview_usuario, parent, false);

        ImageView portada = elemento.findViewById(R.id.imagenUsuario);
        if (datos.get(position).getCodigo()==0) {
            //portada.setImageResource(Integer.parseInt(datos.get(position).getImagen()));
        }else {
            Picasso.get().load(datos.get(position).getImagen()).into(portada);
        }

        TextView nombre = (TextView) elemento.findViewById(R.id.nombreUsuario);
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

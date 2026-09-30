package com.example.spotiyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spotiyou.BBDD.AdaptadorListViewUsuarios;
import com.example.spotiyou.BBDD.BBDD;
import com.example.spotiyou.BBDD.DatosUsuarios;
import com.example.spotiyou.TOKEN.Codigo;

import java.util.ArrayList;

public class ActivityElegirCuenta extends AppCompatActivity {

    private BBDD usuarioBBDD;
    private SQLiteDatabase db;


    private ListView listaUsuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_elegir_cuenta);
        listaUsuarios = findViewById(R.id.listaUsuarios);

        usuarioBBDD = new BBDD(this, "usuarioBBDD", null, 1);
        db = usuarioBBDD.getWritableDatabase();

        if (db != null) {
            if (bbddVacia("usuarioBBDD") == true) {
                ContentValues registro = new ContentValues();
                registro.put("Nombre", "Añadir Usuario");
                registro.put("Imagen", R.drawable.default_pfp);
                registro.put("Estado", 0);
                registro.put("Token", 0);

                db.insert("usuarioBBDD", null, registro);
                db.insert("usuarioBBDD", null, registro);
                db.insert("usuarioBBDD", null, registro);
            }
            Cursor miCursor = db.rawQuery("SELECT codigo,Nombre,Imagen,Estado,Token,RefreshToken FROM usuarioBBDD;", null);
            if (miCursor.moveToFirst()) {
                ArrayList<DatosUsuarios> lista = new ArrayList();
                do {
                    int codigo = miCursor.getInt(0);
                    String nombre = miCursor.getString(1);
                    String imagen = miCursor.getString(2);
                    int estado = miCursor.getInt(3);
                    String token = miCursor.getString(4);
                    String refreshToken = miCursor.getString(5);

                    DatosUsuarios datos = new DatosUsuarios(codigo, nombre, imagen, estado, token, refreshToken);
                    lista.add(datos);

                } while (miCursor.moveToNext());
                AdaptadorListViewUsuarios miAdaptadorListViewUsuarios = new AdaptadorListViewUsuarios(lista, ActivityElegirCuenta.this);
                listaUsuarios.setAdapter(miAdaptadorListViewUsuarios);
            }

            listaUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DatosUsuarios usuario = (DatosUsuarios) listaUsuarios.getItemAtPosition(position);
                    Intent intent = new Intent(ActivityElegirCuenta.this, ConexionSpotifyApi.class);
                    intent.putExtra("estado", usuario.getEstado());
                    Codigo codigo = new Codigo();
                    codigo.setCodigo(usuario.getCodigo());
                    Log.i("PRUEBAIMPORTANTE", String.valueOf(usuario.getCodigo()));
                    startActivity(intent);
                }
            });
        }
    }

    public boolean bbddVacia(String tabla) {
        return DatabaseUtils.queryNumEntries(db, tabla) == 0;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Cursor miCursor = db.rawQuery("SELECT codigo,Nombre,Imagen,Estado,Token,RefreshToken FROM usuarioBBDD;", null);
        if (miCursor.moveToFirst()) {
            ArrayList<DatosUsuarios> lista = new ArrayList();
            do {
                int codigo = miCursor.getInt(0);
                String nombre = miCursor.getString(1);
                String imagen = miCursor.getString(2);
                int estado = miCursor.getInt(3);
                String token = miCursor.getString(4);
                String refreshToken = miCursor.getString(5);

                DatosUsuarios datos = new DatosUsuarios(codigo, nombre, imagen, estado, token, refreshToken);
                lista.add(datos);

            } while (miCursor.moveToNext());
            AdaptadorListViewUsuarios miAdaptadorListViewUsuarios = new AdaptadorListViewUsuarios(lista, ActivityElegirCuenta.this);
            listaUsuarios.setAdapter(miAdaptadorListViewUsuarios);
        }
    }
}
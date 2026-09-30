package com.example.spotiyou;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.spotiyou.BBDD.BBDD;
import com.example.spotiyou.Fragments.BusquedaFragment;
import com.example.spotiyou.Fragments.HomeFragment;
import com.example.spotiyou.Fragments.MusicaFragment.MusicaFragment;
import com.example.spotiyou.TOKEN.Codigo;
import com.example.spotiyou.TOKEN.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import kotlin.UShort;

public class ActivityPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private MusicaFragment musicaFragment = new MusicaFragment();
    private BusquedaFragment busquedaFragment = new BusquedaFragment();

    private DrawerLayout drawerLayout;
    private BBDD bbdd;
    private SQLiteDatabase db;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Codigo codigo = new Codigo();
        userId = codigo.getCodigo();

        bbdd = new BBDD(this, "usuarioBBDD", null, 1);
        db = bbdd.getWritableDatabase();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemIndex = item.getItemId();
                if (itemIndex == R.id.inicio) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).addToBackStack(null).commit();
                    return true;
                }else if (itemIndex == R.id.musica) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,musicaFragment).commit();
                    return true;
                }else if (itemIndex == R.id.busqueda) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,busquedaFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemIndex = item.getItemId();

        if (itemIndex == R.id.nav_logout) {
            showPopup();
            return true;
        }
        return false;
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Desea salir de la cuenta?");
        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues registro = new ContentValues();
                registro.put("Nombre", "Añadir Usuario");
                registro.put("Imagen", R.drawable.default_pfp);
                registro.put("Estado", 0);
                registro.put("Token", 0);

                db.update("usuarioBBDD", registro, "codigo = ?", new String[]{String.valueOf(userId)});
                finish();
            }
        });

        builder.setNegativeButton("Permanecer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
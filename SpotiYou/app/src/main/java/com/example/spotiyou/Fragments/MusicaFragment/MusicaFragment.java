package com.example.spotiyou.Fragments.MusicaFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.spotiyou.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MusicaFragment extends Fragment {


    private BottomNavigationView bottomNavigationView;
    private CancionesFragment cancionesFragment = new CancionesFragment();
    private ArtistasFragment artistasFragment = new ArtistasFragment();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_musica, container, false);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        bottomNavigationView = view.findViewById(R.id.bottom_navigation_song_artist);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_second,cancionesFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemIndex = item.getItemId();
                if (itemIndex == R.id.canciones) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_second,cancionesFragment).commit();
                    return true;
                }else if (itemIndex == R.id.artistas) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_second,artistasFragment).commit();
                    return true;
                }else {
                    return false;
                }
            }
        });
        return view;
    }
}
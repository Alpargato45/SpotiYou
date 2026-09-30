package com.example.spotiyou.Fragments.MusicaFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    private String parentActivityName;
    public MyViewPagerAdapter(@NonNull Fragment fragment,String parentActivityName) {
        super(fragment);
        this.parentActivityName = parentActivityName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return newInstanceCuatro(parentActivityName);
            case 1:
                return newInstanceSeis(parentActivityName);
            case 2:
                return newInstanceVida(parentActivityName);
            default:
                return newInstanceCuatro(parentActivityName);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static CuatroSemanasFragment newInstanceCuatro(String parentActivityName) {
        CuatroSemanasFragment fragment = new CuatroSemanasFragment();
        Bundle args = new Bundle();
        args.putString("parentActivityName", parentActivityName);
        fragment.setArguments(args);
        return fragment;
    }

    public static SeisMesesFragment newInstanceSeis(String parentActivityName) {
        SeisMesesFragment fragment = new SeisMesesFragment();
        Bundle args = new Bundle();
        args.putString("parentActivityName", parentActivityName);
        fragment.setArguments(args);
        return fragment;
    }

    public static DePorVidaFragment newInstanceVida(String parentActivityName) {
        DePorVidaFragment fragment = new DePorVidaFragment();
        Bundle args = new Bundle();
        args.putString("parentActivityName", parentActivityName);
        fragment.setArguments(args);
        return fragment;
    }
}

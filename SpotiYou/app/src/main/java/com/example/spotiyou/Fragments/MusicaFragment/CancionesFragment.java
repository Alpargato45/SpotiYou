package com.example.spotiyou.Fragments.MusicaFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.spotiyou.R;
import com.example.spotiyou.TOKEN.Token;
import com.google.android.material.tabs.TabLayout;

public class CancionesFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private MyViewPagerAdapter myViewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_canciones, container, false);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager2 = view.findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this,"CancionesFragment");
        viewPager2.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        return view;
    }

    //https://stackoverflow.com/questions/72156771/android-viewpager-2-fragment-no-longer-exists-for-key

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewPager2.setAdapter(null);
    }
}
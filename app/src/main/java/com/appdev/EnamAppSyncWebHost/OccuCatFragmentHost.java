package com.appdev.EnamAppSyncWebHost;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class OccuCatFragmentHost extends AppCompatActivity {



    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occu_cat_fragment_host);
        tabLayout = findViewById(R.id.tablayout);



        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.fragment_host, new EngineerFragment());
        ft.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosittion= tab.getPosition();

                if (tabPosittion==0){

                    FragmentManager fm= getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                   ft.add(R.id.fragment_host, new EngineerFragment());
                    ft.commit();


                } else if (tabPosittion==1){

                    FragmentManager fm= getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.add(R.id.fragment_host, new DoctorFragment());
                    ft.commit();

                }  else if (tabPosittion==2){

                    FragmentManager fm= getSupportFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.add(R.id.fragment_host, new TeacherFragment());
                    ft.commit();

                }




            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }


}



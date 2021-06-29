package com.vokasi.mecanic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.fragment.HomeMontir;
import com.vokasi.mecanic.fragment.NotificationMontir;
import com.vokasi.mecanic.fragment.ProfileMontir;

public class HomescreenMontir extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen_montir);

        loadFragment(new HomeMontir());
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationViewMontir);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.berandaMontir:
                        selectedFragment = new HomeMontir();
                        break;
                    case R.id.profileMontir:
                        selectedFragment= new ProfileMontir();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_montir, selectedFragment)
                        .commit();
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_montir, fragment);
        transaction.commit();
    }

}
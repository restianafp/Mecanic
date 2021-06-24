package com.vokasi.mecanic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vokasi.mecanic.adapter.AdapterMontirFull;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.model.UserMontir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilteredMontir extends AppCompatActivity {

    private TextView lokasi;
    private static final int REQUEST_LOCATION_PERMISSION=1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;
    private RecyclerView recyclerView;
    private ArrayList<UserMontir> listMontirFull;
    private AdapterMontirFull adapterMontirFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_montir);

        lokasi = findViewById(R.id.lokasiResultFiltered);
        lokasi.setText(getIntent().getStringExtra("lokasi_user"));
        recyclerView = findViewById(R.id.hasilPencarianFiltered);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(FilteredMontir.this, Locale.getDefault());

        loadMontir();

    }

    private void loadMontir() {
        String spek = getIntent().getStringExtra("spek");
        String loc = lokasi.getText().toString().toLowerCase();
        listMontirFull = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("keahlian", spek).whereEqualTo("kota", loc)
                .addSnapshotListener((value, error) -> {
                    if (error != null){
                        Log.e("FireStore error", error.getMessage());
                        return;
                    }
                    for (DocumentChange dc:value.getDocumentChanges()){
                        if (dc.getType() == DocumentChange.Type.ADDED){
                            listMontirFull.add(dc.getDocument().toObject(UserMontir.class));
                        }
                    }
                    adapterMontirFull = new AdapterMontirFull(FilteredMontir.this,listMontirFull );
                    recyclerView.setAdapter(adapterMontirFull);
                    adapterMontirFull.notifyDataSetChanged();

                });
    }


}
package com.vokasi.mecanic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vokasi.mecanic.adapter.AdapterMontirFull;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.model.UserMontir;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchingPage extends AppCompatActivity {

    private TextView lokasiUser, searchloc;
    private Button cari;
    private SearchView searchET;
    private DatabaseReference mDatabase;
    private AdapterMontirFull adapterMontirFull;
    private RecyclerView recyclerView;
    private ArrayList<UserMontir> listMontirFull;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_page);

        lokasiUser = findViewById(R.id.lokasiResult);
        searchET = findViewById(R.id.searchEDT);
        recyclerView = findViewById(R.id.hasilPencarian);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        lokasiUser.setText(getIntent().getStringExtra("lokasi_user"));
        db = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(SearchingPage.this, LinearLayoutManager.VERTICAL, false));

        listMontirFull = new ArrayList<>();
        adapterMontirFull = new AdapterMontirFull(SearchingPage.this,listMontirFull );
        recyclerView.setAdapter(adapterMontirFull);


        searchET.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void showData() {
        cariKota();
        cariArea();
    }

    private void cariKota() {
        listMontirFull = new ArrayList<>();
        String kueri = searchET.getQuery().toString().toLowerCase();
        String kueriKota = "kota " + kueri;
        String kueriKab = "kab. " + kueri;
        db.collection("Users").whereIn("kota", Arrays.asList(kueriKota, kueriKab))
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
                    adapterMontirFull = new AdapterMontirFull(SearchingPage.this,listMontirFull );
                    recyclerView.setAdapter(adapterMontirFull);
                    adapterMontirFull.notifyDataSetChanged();

                });

    }

    private void cariArea() {
        listMontirFull = new ArrayList<>();
        String kueri = searchET.getQuery().toString().toLowerCase();
        db.collection("Users").whereEqualTo("area", kueri)
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
                    adapterMontirFull = new AdapterMontirFull(SearchingPage.this,listMontirFull );
                    recyclerView.setAdapter(adapterMontirFull);
                    adapterMontirFull.notifyDataSetChanged();

                });

    }


}
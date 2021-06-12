package com.vokasi.mecanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchingPage extends AppCompatActivity {

    private TextView lokasiUser;
    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private DatabaseReference mDatabase;
    private AdapterMontirFull adapterMontirFull;
    private RecyclerView recyclerView;
    private ArrayList<UserMontir> listMontirFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_page);

        lokasiUser = findViewById(R.id.lokasiResult);
        mSearchField = findViewById(R.id.searchEDT);
        mSearchBtn = findViewById(R.id.searchBtn);
        recyclerView = findViewById(R.id.hasilPencarian);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        lokasiUser.setText(getIntent().getStringExtra("lokasi_user"));



        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
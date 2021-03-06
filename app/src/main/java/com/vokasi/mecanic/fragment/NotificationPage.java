package com.vokasi.mecanic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.vokasi.mecanic.R;


public class NotificationPage extends Fragment {
    private View v;
    private RecyclerView recyclerView;
    private EditText searchKota;
    private DatabaseReference mDatabase;


    public NotificationPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.list_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        modelKota = new ArrayList<>();
//        adapterKota = new AdapterKota(getContext(),modelKota);
//        recyclerView.setAdapter(adapterKota);
//
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("daerah").child("kota");
//        mDatabase.orderByChild("kota").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                modelKota.clear();
//
//                for(DataSnapshot ds: snapshot.getChildren()){
//                   String dataKota = ds.getValue(String.class);
//                   ModelKota model = new ModelKota(dataKota);
//
//                    modelKota.add(model);
//
//                }
//                adapterKota.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        return v;
    }
}
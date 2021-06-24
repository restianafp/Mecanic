package com.vokasi.mecanic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vokasi.mecanic.adapter.AdapterMontirFull;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.model.UserMontir;

import java.util.ArrayList;


public class Favorite extends Fragment {

    private  View v;
    private  ArrayList<UserMontir> listMontir;
    private AdapterMontirFull adapterMontirFull;
    private RecyclerView recyclerView;
    public Favorite() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      v = inflater.inflate(R.layout.fragment_favorite, container, false);


      recyclerView = (RecyclerView) v.findViewById(R.id.viewMontirFull);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
      listMontir = new ArrayList<>();
      adapterMontirFull = new AdapterMontirFull(getContext(),listMontir);
      recyclerView.setAdapter(adapterMontirFull);
      loadMontir();
      return  v;
    }

    private void loadMontir() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference refMontir = FirebaseDatabase.getInstance().getReference("Users").child("Favorit").child(user.getUid());
        refMontir.orderByChild("phoneNum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMontir.clear();

                for(DataSnapshot ds: snapshot.getChildren()){
                    UserMontir userMontir = ds.getValue(UserMontir.class);
                    listMontir.add(userMontir);

                }
                adapterMontirFull.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
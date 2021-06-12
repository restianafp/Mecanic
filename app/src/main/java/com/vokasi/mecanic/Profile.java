package com.vokasi.mecanic;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class Profile extends Fragment {


    private View v;
    private TextView namaUser, email, nomorHp;
    private ImageView editBtn, fotoProfil;
    private LinearLayout logout;
    public Profile() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        namaUser = (TextView) v.findViewById(R.id.nama);
        email = (TextView) v.findViewById(R.id.email);
        nomorHp = (TextView) v.findViewById(R.id.nomorHp);
        logout = (LinearLayout) v.findViewById(R.id.logout);
        editBtn =  (ImageView) v.findViewById(R.id.editBtn);
        fotoProfil = (ImageView) v.findViewById(R.id.fotoUser);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                signoutAct();}
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(getContext(), EditProfile.class);
                startActivity(editProfile);

            }
        });

        loadInfo();

        return v;
    }

    private void loadInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Customer");
            ref.orderByChild("email").equalTo(user.getEmail()).limitToFirst(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount()>0) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String namaDepan = "" + ds.child("firstName").getValue();
                            String namaAkhir = " " + ds.child("lastName").getValue();
                            String emailUser = ""+ds.child("email").getValue();
                            String nomorHP = ""+ds.child("phoneNum").getValue();
                            Picasso.with(getContext()).load(ds.child("photo_uri").getValue().toString())
                                    .centerCrop().fit().into(fotoProfil);

                            namaUser.setText(namaDepan + namaAkhir);
                            email.setText(emailUser);
                            nomorHp.setText(nomorHP);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void signoutAct() {
        Intent intent = new Intent(getContext(),LoginPage.class);
        startActivity(intent);
    }
}
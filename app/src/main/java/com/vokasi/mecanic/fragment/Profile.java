package com.vokasi.mecanic.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vokasi.mecanic.activity.EditProfile;
import com.vokasi.mecanic.activity.EmailVerification;
import com.vokasi.mecanic.activity.LoginPage;
import com.vokasi.mecanic.R;


public class Profile extends Fragment {


    private View v;
    private TextView namaUser, email, nomorHp;
    private ImageView editBtn, fotoProfil;
    private LinearLayout logout, verif,setLanguage;
    private FirebaseAuth firebaseAuth;
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
        verif = v.findViewById(R.id.verifiedAcc);
        setLanguage = (LinearLayout) v.findViewById(R.id.settingLanguageCust);
        editBtn =  (ImageView) v.findViewById(R.id.editBtn);
        fotoProfil = (ImageView) v.findViewById(R.id.fotoUser);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                if (FirebaseAuth.getInstance().getCurrentUser()==null){
                    signoutAct();
                }
            }
        });

        verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EmailVerification.class);
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(getContext(), EditProfile.class);
                startActivity(editProfile);

            }
        });

        setLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
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
                            String namaDepan = "" + ds.child("firstName").getValue().toString();
                            String emailUser = ""+ds.child("email").getValue().toString();
                            String nomorHP = ""+ds.child("phoneNum").getValue().toString();
                            Picasso.with(getContext()).load(ds.child("photo_uri").getValue().toString())
                                    .centerCrop().fit().into(fotoProfil);

                            namaUser.setText(namaDepan);
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
        Intent intent = new Intent(getContext(), LoginPage.class);
        startActivity(intent);
        getActivity().finish();
    }

}
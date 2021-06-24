package com.vokasi.mecanic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.activity.EditProfile;
import com.vokasi.mecanic.activity.EditProfileMontir;
import com.vokasi.mecanic.activity.EmailVerification;
import com.vokasi.mecanic.activity.LoginPage;


public class ProfileMontir extends Fragment {

    private View v;
    private TextView namaUser, email, nomorHp;
    private ImageView editBtn, fotoProfil;
    private LinearLayout logout, verif, setLanguage;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String docID, uriPhoto ;



    public ProfileMontir() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile_montir, container, false);
        namaUser = (TextView) v.findViewById(R.id.namaMontirProfile);
        email = (TextView) v.findViewById(R.id.emailMontirProfile);
        nomorHp = (TextView) v.findViewById(R.id.nomorHpMontirProfile);
        logout = (LinearLayout) v.findViewById(R.id.logoutMontir);
        setLanguage = (LinearLayout) v.findViewById(R.id.settingLanguage);
        verif = v.findViewById(R.id.verifiedAccMontirProfile);
        editBtn =  (ImageView) v.findViewById(R.id.editBtnMontirProfile);
        fotoProfil = (ImageView) v.findViewById(R.id.fotoMontirProfile);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

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

        setLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoEdit();

            }
        });


        loadInfo();



        return v;
    }

    private void gotoEdit() {
        Intent editProfile = new Intent(getContext(), EditProfileMontir.class);
        startActivity(editProfile);
    }

    private void loadInfo() {
        if (user != null) {
            db.collection("Users").whereEqualTo("email", user.getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null){
                        Log.e("FireStore error", error.getMessage());
                        return;
                    }else{
                        for (DocumentSnapshot doc:value){
                            docID = doc.getId();
                        }
                        getMontirInfo();
                    }
                }
            });

        }

    }

    private void getMontirInfo(){
            db.collection("Users").document(docID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value!=null){
                        String namaDepan = value.getString("firstName");
                        String namaBelakang = value.getString("lastName");
                        uriPhoto = value.getString("photo_Uri");
                        String phoneNum = value.getString("phoneNum");

                        namaUser.setText(namaDepan);
                        email.setText(user.getEmail());
                        nomorHp.setText(phoneNum);
                        Picasso.with(getContext()).load(uriPhoto).fit().centerCrop().into(fotoProfil);

                    }
                }
            });

    }

    private void signoutAct() {
            Intent intent = new Intent(getContext(), LoginPage.class);
            startActivity(intent);
            getActivity().finish();
    }

}
package com.vokasi.mecanic.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.model.UserMontir;

import java.util.ArrayList;
import java.util.Locale;

public class DetailedMontirInfo extends AppCompatActivity {
    private TextView nama, spek, bengkelTv, jamOperasi, nomorHp, lokasi, ratingBar;
    private ImageView fotoMontir, favBtn;
    private ImageButton backBtn;
    private Button waBtn, reviewBtn;
    private float ratingInput;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private String newRate, id;
    private String accType, email,password, firstName, keahlian,bengkel,kota,area, alamat, buka, tutup, phoneNum, photo_uri, rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_montir_info);
        nama = findViewById(R.id.namaMontirDetail);
        spek = findViewById(R.id.spekDetail);
        bengkelTv = findViewById(R.id.bengkelDetail);
        jamOperasi = findViewById(R.id.jamDetail);
        nomorHp = findViewById(R.id.nomorDetail);
        fotoMontir = findViewById(R.id.fotoMontirDetail);
        lokasi = findViewById(R.id.lokasiDetail);
        backBtn = findViewById(R.id.backBtnDetail);
        waBtn = findViewById(R.id.btn_wa);
        favBtn = findViewById(R.id.favBtn);
        ratingBar = findViewById(R.id.ratingDetail);
        reviewBtn = findViewById(R.id.btn_review);

        accType ="Montir";
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("pass");
        firstName = getIntent().getStringExtra("namaDepan");
        keahlian = getIntent().getStringExtra("spek");
        bengkel = getIntent().getStringExtra("bengkel");
        kota = getIntent().getStringExtra("kota").toUpperCase();
        area = getIntent().getStringExtra("area").toUpperCase();
        alamat = getIntent().getStringExtra("alamat").toUpperCase();
        buka = getIntent().getStringExtra("jamBuka");
        tutup = getIntent().getStringExtra("jamTutup");
        phoneNum = getIntent().getStringExtra("nomor");
        photo_uri = getIntent().getStringExtra("fotoUrl");
        rating = getIntent().getStringExtra("rating");

        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFav();
            }
        });
        waBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimPesan();
            }
        });
//       ratingBarDetail.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//           @Override
//           public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//
//               saveRating();
//           }
//       });

       reviewBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showBottomSheet();

           }
       });



        loadData();

        setIcon();

    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                DetailedMontirInfo.this, R.style.BottomSheetDialogTheme);
        View bottomsheetview = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (LinearLayout) findViewById(R.id.bottomSheetContainer)
                );
        bottomsheetview.findViewById(R.id.btn_submit_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar rating = bottomsheetview.findViewById(R.id.ratingBarInput);
                ratingInput = rating.getRating();
                if (ratingInput!=0.0) {
                    saveRating();
                }

            }
            private void saveRating() {
                Float rate = ratingInput;
                String r = rate.toString();
                FirebaseDatabase.getInstance().getReference("Rating").child(phoneNum).child(user.getUid()).child("ratings").setValue(r);
                sumRating();
            }

            private float ratingSum = 0;

            private void sumRating() {
                FirebaseDatabase.getInstance().getReference("Rating").child(phoneNum).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            float ratingDb =  Float.parseFloat(""+ds.child("ratings").getValue());
                            ratingSum = ratingSum + ratingDb;
                        }
                        long numberReviews = snapshot.getChildrenCount();
                        Float text = ratingSum/numberReviews;
                        newRate = text.toString();
                        savedFirestore();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            private void savedFirestore() {
                CollectionReference mref = FirebaseFirestore.getInstance().collection("Users");
                mref.whereEqualTo("email",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot doc:value){
                            id = doc.getId();
                        }
                        mref.document(id).update("rating", newRate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(DetailedMontirInfo.this, getString(R.string.sukses_rating), Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        });
                    }
                });


            }
        });
        bottomSheetDialog.setContentView(bottomsheetview);
        bottomSheetDialog.show();
    }


    private void setIcon() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users").child("Favorit")
                .child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(phoneNum)){
                    favBtn.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                } else {
                    favBtn.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkFav() {
        FirebaseDatabase.getInstance().getReference("Users").child("Favorit").child(user.getUid())
                .child(getIntent().getStringExtra("nomor"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        ds.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                favBtn.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                            }
                        });
                    }

                } else {
                    simpanFavorit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void simpanFavorit() {

        progressDialog.setMessage(getString(R.string.add_fav));
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserMontir montirData = new UserMontir(accType, email, password, firstName,keahlian, bengkel,kota, area, alamat, buka
                , tutup, phoneNum, photo_uri,rating);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("Favorit")
                .child(user.getUid());
        mDatabase.child(phoneNum).setValue(montirData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                favBtn.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(DetailedMontirInfo.this, "" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(){
        nama.setText(firstName);
        bengkelTv.setText(bengkel);
        jamOperasi.setText(buka + "-" + tutup);
        nomorHp.setText(phoneNum);
        lokasi.setText(alamat.toUpperCase());
        Picasso.with(this).load(photo_uri).centerCrop().fit().into(fotoMontir);
        ratingBar.setText(rating);
        if (Locale.getDefault().getDisplayLanguage().startsWith("English")){
            if (rating.startsWith("Motor")){
                spek.setText(getString(R.string.spesialis) + " " +"Motorcycle");
            }else {
                spek.setText(getString(R.string.spesialis) + " " +"Car");
            }
        }else {
            spek.setText(getString(R.string.spesialis) + " " +keahlian);
        }

        progressDialog.dismiss();


    }

    private void kirimPesan() {
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+62"+ phoneNum+ "&text=" + getString(R.string.pesan_wa)));
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.whatsapp"));
            startActivity(intent);
            finish();

        }

    }

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager = getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return  app_installed;
    }
}
package com.vokasi.mecanic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfile extends AppCompatActivity {

    private ImageButton  saveBtn, backBtn;
    private ImageView addPict;
    private ImageView fotoProfile;
    private EditText emaailEt, namaDepan, namaBelakang, nomorWA;

    private Uri photo_location;
    Integer photo_max = 1;
    StorageReference storage;
    DatabaseReference ref;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        addPict = findViewById(R.id.addPicBtn);
        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn2);
        fotoProfile = findViewById(R.id.photoProfile);
        emaailEt = findViewById(R.id.emailEdit);
        namaDepan = findViewById(R.id.firstNameEdit);
        namaBelakang = findViewById(R.id.lastNameEdit);
        nomorWA = findViewById(R.id.phoneNumEdit);
        ref = FirebaseDatabase.getInstance().getReference("Users");
        storage = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        addPict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loadInfo();
    }


    private void loadInfo() {
        if (firebaseUser != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Customer");
            ref.orderByChild("email").equalTo(firebaseUser.getEmail()).limitToFirst(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount()>0) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String firstName = "" + ds.child("firstName").getValue();
                            String namaAkhir = " " + ds.child("lastName").getValue();
                            String emailUser = ""+ds.child("email").getValue();
                            String nomorHP = ""+ds.child("phoneNum").getValue();
                            Picasso.with(EditProfile.this).load(ds.child("photo_uri").getValue().toString())
                                    .fit().centerCrop().into(fotoProfile);

                            namaDepan.setText(firstName);
                            namaBelakang.setText(namaAkhir);
                            emaailEt.setText(emailUser);
                            nomorWA.setText(nomorHP);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void updateData() {
        String nama1 = namaDepan.getText().toString();
        String nama2 = namaBelakang.getText().toString();
        String nomor = nomorWA.getText().toString();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(firebaseUser.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child("firstName").setValue(nama1);
                snapshot.getRef().child("lastName").setValue(nama2);
                snapshot.getRef().child("phoneNum").setValue(nomor);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        updatePhoto();
    }

    private void updatePhoto() {
        String email = firebaseUser.getEmail();
        storage = storage.child("PhotoUsers").child(email);
            if (photo_location != null){
                StorageReference storageReference = storage.child(System.currentTimeMillis() + "."
                + getFileExt(photo_location));
                UploadTask uploadTask = storageReference.putFile(photo_location);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        String uri_photo = uri.toString();
                        String nama1 = namaDepan.getText().toString();
                        String nama2 = namaBelakang.getText().toString();
                        if (firebaseUser != null) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Customer");
                            ref.child(firebaseUser.getUid()).child("photo_uri").setValue(uri_photo);
                        }

                    }

                });
            }
            Intent intent = new Intent(EditProfile.this, Homescreen.class);
            startActivity(intent);
    }

    String getFileExt(Uri uri){
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data!= null && data.getData()!= null){
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(fotoProfile);
        }
    }
}
package com.vokasi.mecanic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.fragment.Profile;

public class EditProfile extends AppCompatActivity {

    private ImageButton  backBtn;
    private Button saveBtn;
    private ImageView addPict, btnNama, btnNomor;
    private ImageView fotoProfile;
    private EditText emaailEt, nama, nomorWA;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID=0;
    private static final String CHANNEL_ID="ch1";

    private Uri photo_location;
    private ProgressDialog progressDialog;
    Integer photo_max = 1;
    StorageReference storage;
    DatabaseReference ref;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        addPict = findViewById(R.id.addPicBtnCust);
        backBtn = findViewById(R.id.backBtnCustEdit);
        fotoProfile = findViewById(R.id.fotoProfileCust);
        emaailEt = findViewById(R.id.emailCustEdit);
        nama = findViewById(R.id.namaCustEdit);
        btnNama = findViewById(R.id.btnEditNamaCust);
        btnNomor= findViewById(R.id.btnEditNomorCust);
        nomorWA = findViewById(R.id.nomorCustEdit);
        saveBtn = findViewById(R.id.btnSaveEditCust);
        ref = FirebaseDatabase.getInstance().getReference("Users");
        storage = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);




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

        btnNomor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocusEdittext();
                setFocusEdittext(nomorWA);
            }
        });

        btnNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocusEdittext();
                setFocusEdittext(nama);

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
                            String firstName = "" + ds.child("firstName").getValue().toString();
                            String emailUser = "" + ds.child("email").getValue().toString();
                            String nomorHP = "" + ds.child("phoneNum").getValue().toString();
                            Picasso.with(EditProfile.this).load(ds.child("photo_uri").getValue().toString())
                                    .fit().centerCrop().into(fotoProfile);

                            nama.setText(firstName);
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

    private void clearFocusEdittext(){
        nama.setClickable(false);
        nomorWA.setClickable(false);
        nama.setFocusable(false);
        nomorWA.setFocusable(false);
    }

    private void setFocusEdittext(EditText s) {
        s.setFocusableInTouchMode(true);
        s.requestFocus();
        InputMethodManager imm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    private void updateData() {
        progressDialog.setMessage(getString(R.string.save_newData_pg));
        progressDialog.show();
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
                        String nama1 = nama.getText().toString();
                        String nomor = nomorWA.getText().toString();
                        if (firebaseUser != null) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Customer").child(firebaseUser.getUid());
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.getRef().child("firstName").setValue(nama1);
                                    snapshot.getRef().child("phoneNum").setValue(nomor);
                                    snapshot.getRef().child("photo_uri").setValue(uri_photo);
                                    progressDialog.dismiss();
                                    notifySuccess();
                                    Toast.makeText(getApplicationContext(), R.string.updated_data, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();

                                }
                            });
                        }
                    }

                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        progressDialog.dismiss();
                    }
                });

            }
    }

    private void notifySuccess() {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    CHANNEL_ID)
                    .setContentTitle(getResources().getText(R.string.app_name))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(getString(R.string.succes_edit))
                    .setColor(getResources().getColor(R.color.mainColor));
            Notification notification = builder.build();
            notificationManager.notify(NOTIFICATION_ID, notification);
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
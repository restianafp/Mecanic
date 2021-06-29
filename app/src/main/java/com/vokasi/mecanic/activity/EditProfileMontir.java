package com.vokasi.mecanic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.vokasi.mecanic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfileMontir extends AppCompatActivity {
    private EditText namaUser, nomorHp, email ;
    private ImageView btnNama, btnNomor, btnSpek, fotoProfil, addPict;
    private Button saveBtn ;
    private Uri photo_location;
    private Spinner spekMontir;
    private List<String> list;
    private ArrayAdapter arrayAdapter;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private String docID, uriPhoto, motor, mobil;
    private StorageReference storage;

    Integer photo_max = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_montir);

        namaUser = findViewById(R.id.namaMontirEdit);
        nomorHp = findViewById(R.id.nomorMontirEdit);
        email = findViewById(R.id.emailMontirEdit);
        spekMontir = findViewById(R.id.spekMontirEdit);
        fotoProfil = findViewById(R.id.fotoProfileMontir);
        btnNama = findViewById(R.id.btnEditNamaMontir);
        btnNomor= findViewById(R.id.btnEditNomorMontir);
        btnSpek = findViewById(R.id.btnEditSpekMontir);
        saveBtn = findViewById(R.id.btnSaveEditMontir);
        addPict = findViewById(R.id.addPicBtnMontir);
        mobil = getResources().getString(R.string.mobil);
        motor= getResources().getString(R.string.motor);
        list = new ArrayList<String>();
        list.add(motor);
        list.add(mobil);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        spekMontir.setEnabled(false);

        btnNomor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocusEdittext();
                setFocusEdittext(nomorHp);
            }
        });

        btnNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFocusEdittext();
                setFocusEdittext(namaUser);

            }
        });

        btnSpek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spekMontir.setClickable(true);
                spekMontir.setEnabled(true);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoto();
            }
        });

        addPict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });


        arrayAdapter = new ArrayAdapter(this, R.layout.spinner_kota, list);
        arrayAdapter.setDropDownViewResource(R.layout.card_kota);
        spekMontir.setAdapter(arrayAdapter);

        loadInfo();

    }

    private void saveFirestore() {
        Integer opsi = spekMontir.getSelectedItemPosition();
        String nama = namaUser.getText().toString();
        String nomor = nomorHp.getText().toString();
        Map<String, Object> montir = new HashMap<>();
        montir.put("firstName", nama);
        montir.put("phoneNum", nomor);
        montir.put("photo_Uri", uriPhoto);
        if (opsi==0){
            montir.put("keahlian", "Motor");
        }else {
            montir.put("keahlian", "Mobil");
        }
        if (docID!=null){
            db.collection("Users").document(docID).update(montir).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), R.string.updated_data, Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    private void updatePhoto() {
        if (user!=null){
            storage = storage.child("PhotoUsers").child(user.getEmail());
            if (photo_location != null){
                storage = storage.child(System.currentTimeMillis() + "."
                        + getFileExt(photo_location));
                UploadTask uploadTask = storage.putFile(photo_location);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return storage.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri uri = task.getResult();
                        uriPhoto = uri.toString();
                        saveFirestore();


                    }

                });

            }

        }

    }

    private void loadInfo() {
        if (user!= null) {
            db.collection("Users").whereEqualTo("email", user.getEmail())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    uriPhoto = value.getString("photo_Uri");
                    String phoneNum = value.getString("phoneNum");
                    String keahlian = value.getString("keahlian");

                    namaUser.setText(namaDepan);
                    email.setText(user.getEmail());
                    nomorHp.setText(phoneNum);
                    Picasso.with(getApplicationContext()).load(uriPhoto).fit().centerCrop().into(fotoProfil);
                    if (keahlian.startsWith("Motor")){
                        spekMontir.setSelection(0);
                    }else{
                        spekMontir.setSelection(1);
                    }
                }
            }
        });

    }

    private void clearFocusEdittext(){
        namaUser.setClickable(false);
        nomorHp.setClickable(false);
        namaUser.setFocusable(false);
        nomorHp.setFocusable(false);
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
            Picasso.with(this).load(photo_location).centerCrop().fit().into(fotoProfil);
        }
    }




}
package com.vokasi.mecanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistMontirNext extends AppCompatActivity implements
        FetchAddress.OnTaskCompleted , AdapterView.OnItemSelectedListener {

    private Button daftar;
    private ImageButton gpsBtn;
    private Spinner spinner;
    private String item, item2;
    private TextView kotaTv;
    private EditText bengkelEt, jamBuka, jamTutup, alamatEt, phoneNumEtMont;
    TimePickerDialog timePickerDialog;
    Dialog dialog;
    private static final int REQUEST_LOCATION_PERMISSION=1;
    FusedLocationProviderClient fusedLocationProviderClient;
    private String[] spesialisasi ={"Pilih spesialisasi keahlian", "Motor", "Mobil"};
    private ArrayAdapter arrayAdapter;



    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_montir_next);

        daftar = findViewById(R.id.btn_daftar2);
        bengkelEt = findViewById(R.id.bengkelEt);
        alamatEt = findViewById(R.id.alamatEt);
        jamBuka = findViewById(R.id.jamBuka);
        jamTutup = findViewById(R.id.jamTutup);
        spinner= findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        gpsBtn = findViewById(R.id.gpsBtn);
        phoneNumEtMont= findViewById(R.id.phoneNumEtMont);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        arrayAdapter = new ArrayAdapter(this, R.layout.spinner_kota, spesialisasi);
        arrayAdapter.setDropDownViewResource(R.layout.card_kota);
        spinner.setAdapter(arrayAdapter);

        firebaseAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("daerah").child("kota");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon tunggu");
        progressDialog.setCanceledOnTouchOutside(false);


        gpsBtn.setOnClickListener(v -> getLocation());

        jamBuka.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(RegistMontirNext.this, (timePicker, hourOfDay, minutes) -> jamBuka.setText(String.format("%02d:%02d", hourOfDay, minutes)), 0, 0, true);

            timePickerDialog.show();
        });
        jamTutup.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(RegistMontirNext.this, (timePicker, hourOfDay, minutes) -> jamTutup.setText(String.format("%02d:%02d", hourOfDay, minutes)), 0, 0, true);

            timePickerDialog.show();
        });

        daftar.setOnClickListener(v -> inputData());


    }



    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else{
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(),
                    new LocationCallback(){
                        @Override
                        public void onLocationResult(LocationResult locationResult){
                            Location location=locationResult.getLastLocation();
                            if(location!=null){
                                try {
                                    Geocoder geocoder = new Geocoder(RegistMontirNext.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(),1
                                    );
                                    new FetchAddress(RegistMontirNext.this, RegistMontirNext.this).execute(location);
                                }
                                catch (IOException e){
                                    e.printStackTrace();}
                            }
                        }

                    }, null);

        }

        }


    private LocationRequest getLocationRequest(){
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                //pertanyaan jika disetujui sama user apa engga
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //jika disetujui maka:
                    getLocation();
                }else{
                    //jika tidak maka:
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        alamatEt.setText(result);
    }


    private String accType, email, username, password,  firstName, lastName,
            keahlian, bengkel, kota, alamat, buka, tutup, phoneNum,photo_uri,rating;
    private void inputData() {
        accType = getIntent().getStringExtra("data_accType");
        email = getIntent().getStringExtra("data_Email");
        username = getIntent().getStringExtra("data_Username");
        password = getIntent().getStringExtra("data_Pass");
        firstName = getIntent().getStringExtra("data_FirstName");
        lastName = getIntent().getStringExtra("data_LastName");
        keahlian = item;
        bengkel = bengkelEt.getText().toString().trim();
        alamat = alamatEt.getText().toString().trim();
        buka = jamBuka.getText().toString();
        tutup = jamTutup.getText().toString();
        phoneNum = phoneNumEtMont.getText().toString();
        photo_uri = "https://firebasestorage.googleapis.com/v0/b/mecanic-7161d.appspot.com/o/icon_nopic.png?alt=media&token=1c0d4374-837d-4f94-bf94-34110169ff85";

        createAccount();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = spinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createAccount() {
        progressDialog.setMessage("Membuat akun...");
        progressDialog.show();

        //create account

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(authResult -> {
                    //berhasil dibuat
                    savedFirebaseData();
                }).addOnFailureListener(e -> {
                    //gagal dibuat
                    progressDialog.dismiss();
                    Toast.makeText(RegistMontirNext.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void savedFirebaseData() {
        progressDialog.setMessage("Menyimpan info akun...");

        Map<String, Object> montir = new HashMap<>();
        montir.put("accType", accType);
        montir.put("email", email);
        montir.put("firstName", firstName);
        montir.put("lastName", lastName);
        montir.put("keahlian", keahlian);
        montir.put("bengkel", bengkel);
        montir.put("alamat", alamat);
        montir.put("buka", buka);
        montir.put("tutup", tutup);
        montir.put("phoneNum", phoneNum);
        montir.put("photo_Uri", photo_uri);

        FirebaseFirestore.getInstance().collection("Users").add(montir).addOnSuccessListener(documentReference -> {
            progressDialog.dismiss();
            Intent loginSucces = new Intent(RegistMontirNext.this, LoginPage.class);
            startActivity(loginSucces);

        });



    }





}





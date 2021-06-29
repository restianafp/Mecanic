package com.vokasi.mecanic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vokasi.mecanic.R;

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
    private String item, Kota, Area, motor, mobil;
    private TextView alamatEt, jamBuka, jamTutup;
    private EditText bengkelEt, phoneNumEtMont;
    TimePickerDialog timePickerDialog;
    Dialog dialog;
    private static final int REQUEST_LOCATION_PERMISSION=1;
    FusedLocationProviderClient fusedLocationProviderClient;
    private List<String> list;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> arrayList;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID=0;
    private static final String CHANNEL_ID="ch1";



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
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);


        mobil = getResources().getString(R.string.mobil);
        motor= getResources().getString(R.string.motor);
        list = new ArrayList<String>();
        list.add(motor);
        list.add(mobil);
        arrayAdapter = new ArrayAdapter(this, R.layout.spinner_kota, list);
        arrayAdapter.setDropDownViewResource(R.layout.card_kota);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0, false);

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

        alamatEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });


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
                                            location.getLatitude(), location.getLongitude(),1);
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
    public void onTaskCompleted(ArrayList<String> result) {
        String alamat = TextUtils.join(",",result);
        ArrayList<String> area = new ArrayList<>();
        area.add(result.get(1));
        area.add(result.get(2));
        Kota = result.get(3);
        Area = result.get(2);

        alamatEt.setText(alamat);

    }


    private String accType, email, password,  firstName,
            keahlian, bengkel, kota,area, alamat, buka, tutup, phoneNum,photo_uri,rating;
    private void inputData() {
        accType = getIntent().getStringExtra("data_accType");
        email = getIntent().getStringExtra("data_Email");
        password = getIntent().getStringExtra("data_Pass");
        firstName = getIntent().getStringExtra("data_FirstName");
        keahlian = item;
        bengkel = bengkelEt.getText().toString().trim();
        kota = Kota.toLowerCase();
        area = Area.toLowerCase();
        alamat = alamatEt.getText().toString().trim();
        buka = jamBuka.getText().toString();
        tutup = jamTutup.getText().toString();
        phoneNum = phoneNumEtMont.getText().toString();
        photo_uri = "https://firebasestorage.googleapis.com/v0/b/mecanic-7161d.appspot.com/o/no-user-image-icon-27.jpg?alt=media&token=6a6d564b-dcdc-42c2-afb9-60849ef3dbf3";
        rating = "0.0";

        createAccount();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Integer opsi = spinner.getSelectedItemPosition();
        if (opsi==0){
            item = "Motor";
        }else {
            item="Mobil";
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createAccount() {


        //create account
        progressDialog.setMessage("Membuat akun...");
        progressDialog.show();
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
        montir.put("keahlian", keahlian);
        montir.put("bengkel", bengkel);
        montir.put("kota", kota);
        montir.put("area", area);
        montir.put("alamat", alamat);
        montir.put("buka", buka);
        montir.put("tutup", tutup);
        montir.put("phoneNum", phoneNum);
        montir.put("photo_Uri", photo_uri);
        montir.put("rating", rating);

        FirebaseFirestore.getInstance().collection("Users").add(montir).addOnSuccessListener(documentReference -> {
            progressDialog.dismiss();
            Intent loginSucces = new Intent(RegistMontirNext.this, LoginPage.class);
            startActivity(loginSucces);

        });



    }

    private void notifySuccess() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                CHANNEL_ID)
                .setContentTitle(getResources().getText(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getResources().getText(R.string.notif_sukses))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText(getString(R.string.notif_sumary_sukses))
                        .setBigContentTitle(getResources().getText(R.string.verifikasi_akun)))
                .setColor(getResources().getColor(R.color.mainColor));
        Notification notification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }





}





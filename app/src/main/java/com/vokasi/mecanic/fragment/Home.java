package com.vokasi.mecanic.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.Address;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.vokasi.mecanic.activity.FetchAddress;
import com.vokasi.mecanic.activity.FetchArea;
import com.vokasi.mecanic.activity.RegistMontirNext;
import com.vokasi.mecanic.adapter.AdapterMontir;
import com.vokasi.mecanic.activity.FilteredMontir;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.activity.SearchingPage;
import com.vokasi.mecanic.model.UserMontir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends Fragment {

    private View homeView;
    private ImageButton btnLokasi;
    private ImageView fotoProfil;
    private ConstraintLayout mobil, motor;
    private TextView namaUser, lokasiUser, searchBar;
    private RecyclerView rekomMontir;
    private FirebaseAuth firebaseAuth;
    private ArrayList<UserMontir> listMontir;
    private AdapterMontir adapterMontir;
    private Geocoder geocoder;
    private String hello;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;
    private static final int REQUEST_LOCATION_PERMISSION=1;
    FusedLocationProviderClient fusedLocationProviderClient;
    private boolean executed;




    public Home() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        namaUser =  homeView.findViewById(R.id.namaUser);
        lokasiUser =  homeView.findViewById(R.id.lokasiUser);
        searchBar =  homeView.findViewById(R.id.searchBar);
        btnLokasi = homeView.findViewById(R.id.btnLocation);
        fotoProfil = homeView.findViewById(R.id.fotoProfil);
        mobil = homeView.findViewById(R.id.mobil);
        motor = homeView.findViewById(R.id.motor);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        geocoder = new Geocoder(getContext(),Locale.getDefault());
        db = FirebaseFirestore.getInstance();


        rekomMontir = homeView.findViewById(R.id.viewMontir);
        rekomMontir.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        hello = getActivity().getString(R.string.hello);

        listMontir = new ArrayList<>();
        adapterMontir = new AdapterMontir(getContext(),listMontir );
        rekomMontir.setAdapter(adapterMontir);
        lokasiUser.setText("Memuat lokasi");

        loadMyInfo();
        loadMontirInfo();
        loadUserLocation();


        searchBar.setOnClickListener(v -> nextSearch());

        mobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = lokasiUser.getText().toString();
                Intent nextPage = new Intent(getContext(), FilteredMontir.class)
                        .putExtra("spek", "Mobil").putExtra("lokasi_user", loc);
                startActivity(nextPage);

            }
        });

        motor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = lokasiUser.getText().toString();
                Intent nextPage = new Intent(getContext(),FilteredMontir.class)
                        .putExtra("spek", "Motor").putExtra("lokasi_user", loc);
                startActivity(nextPage);

            }
        });


        return homeView;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    private void loadUserLocation() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else{
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            //jika ada data lokasi
                            if(location!=null){
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(),1
                                    );
                                    String loc = addresses.get(0).getSubAdminArea();
                                    lokasiUser.setText(loc);
                                }
                                catch (IOException e){
                                    e.printStackTrace();
                                }
                            } else{
                                //jika tidak ada data lokasi
                                lokasiUser.setText(R.string.data_not_found);
                            }
                        }
                    }
            );
        }
    }

    private void nextSearch() {
        String loc = lokasiUser.getText().toString();
        Intent nextPage = new Intent(getContext(), SearchingPage.class)
                .putExtra("lokasi_user", loc);
        startActivity(nextPage);
    }

    private LocationRequest getLocationRequest() {
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
                    loadUserLocation();
                }else{
                    //jika tidak maka:
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void loadMontirInfo() {
        db.collection("Users").addSnapshotListener((value, error) -> {
            if (error != null){
                Log.e("FireStore error", error.getMessage());
                return;
            }
            for (DocumentChange dc:value.getDocumentChanges()){
                if (dc.getType() == DocumentChange.Type.ADDED){
                listMontir.add(dc.getDocument().toObject(UserMontir.class));
                }
            }
            adapterMontir.notifyDataSetChanged();

        });


    }

    private void loadMyInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!= null) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Customer");
        ref.orderByChild("email").equalTo(user.getEmail()).limitToFirst(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount()>0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String namaDepan = "" + ds.child("firstName").getValue();
                        Picasso.with(getContext()).load(ds.child("photo_uri").getValue().toString())
                                .centerCrop().fit().into(fotoProfil);
                        namaUser.setText(hello + namaDepan);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        }
    }



}
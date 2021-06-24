package com.vokasi.mecanic.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.vokasi.mecanic.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class HomeMontir extends Fragment {

    private View homeview;
    private ImageButton btnLokasi;
    private ImageView fotoProfil;
    private TextView namaUser, lokasiUser, rating, spek;
    private FirebaseUser user;
    private Geocoder geocoder;
    private String docID;
    private FirebaseFirestore db;
    private String hello;
    private static final int REQUEST_LOCATION_PERMISSION=1;
    FusedLocationProviderClient fusedLocationProviderClient;

    public HomeMontir() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeview = inflater.inflate(R.layout.fragment_home_montir, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        namaUser =  homeview.findViewById(R.id.namaUserMontir);
        lokasiUser =  homeview.findViewById(R.id.lokasiUserMontir);
        btnLokasi = homeview.findViewById(R.id.btnLocationMontir);
        fotoProfil = homeview.findViewById(R.id.fotoProfilMontir);
        rating = homeview.findViewById(R.id.ratingMontir);
        spek = homeview.findViewById(R.id.spekMontir);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        db = FirebaseFirestore.getInstance();
        hello = getActivity().getString(R.string.hello);

        loadMyInfo();
        loadUserLocation();



        return  homeview;
    }



    private void loadMyInfo() {
        if(user!=null){
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
                    if (value != null){
                        String namaDepan = value.getString("firstName");
                        String photo_uri = value.getString("photo_Uri");
                        String ratingDb = value.getString("rating");
                        String spekDb = value.getString("keahlian");

                        namaUser.setText(hello+" "+namaDepan);
                        Picasso.with(getContext()).load(photo_uri).fit().centerCrop().into(fotoProfil);
                        rating.setText(ratingDb);
                        spek.setText(spekDb);

                    }


                }
            });


    }

    private void loadUserLocation() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else{
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(),
                    new LocationCallback(){
                        @Override
                        public void onLocationResult(LocationResult locationResult){
                            Location location=locationResult.getLastLocation();
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

                            }
                        }

                    },null );
        }
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


}
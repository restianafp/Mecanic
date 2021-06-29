package com.vokasi.mecanic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vokasi.mecanic.R;

public class MainActivity extends AppCompatActivity {
    public ImageView logoapp;
    private FirebaseUser firebaseAuth;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID=0;
    private static final String CHANNEL_ID="ch1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoapp=findViewById(R.id.logoapp);
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkType();
            }
        } ,2000);
    }

    private void checkType() {
        if (firebaseAuth!= null) {
            FirebaseDatabase.getInstance().getReference("Users").child("Customer")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(firebaseAuth.getUid())) {
                        if (!firebaseAuth.isEmailVerified()){
                            notifyVerif();
                        }
                            startActivity(new Intent(getApplicationContext(), Homescreen.class));
                            finish();
                    }else {
                        if (!firebaseAuth.isEmailVerified()){
                            notifyVerif();
                        }
                            startActivity(new Intent(getApplicationContext(), HomescreenMontir.class));
                            finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Intent gotologin = new Intent(MainActivity.this, LoginPage.class);
            startActivity(gotologin);
            finish();
    }

}


    private void notifyVerif() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                CHANNEL_ID)
                .setContentTitle(getResources().getText(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(getString(R.string.verif_email))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText(getString(R.string.verifikasi_akun))
                        .setBigContentTitle(getResources().getText(R.string.verifikasi_akun)))
                .setColor(getResources().getColor(R.color.mainColor));
        Notification notification = builder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }


}
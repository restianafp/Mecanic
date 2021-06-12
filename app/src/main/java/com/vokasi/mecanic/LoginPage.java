package com.vokasi.mecanic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    private TextView daftar;
    private EditText email, password;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID=0;
    private static final String CHANNEL_ID="ch1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        daftar = findViewById(R.id.daftar);
        btnLogin = findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);


        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoRolepage = new Intent(LoginPage.this, RolePage.class);
                startActivity(gotoRolepage);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract
                if (email.getText().toString().isEmpty()) {
                    email.setError("Email tidak valid");
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    password.setError("Password tidak valid");
                    return;
                }

                //login
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(getApplicationContext(), Homescreen.class).putExtra("email", email.getText().toString()));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Homescreen.class));
            finish();
        }
    }
}




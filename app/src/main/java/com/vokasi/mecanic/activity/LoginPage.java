package com.vokasi.mecanic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vokasi.mecanic.R;

public class LoginPage extends AppCompatActivity {

    private TextView daftar;
    private EditText email, password;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID=0;
    private static final String CHANNEL_ID="ch1";
    private ProgressDialog progressDialog;


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
                progressDialog = new ProgressDialog(LoginPage.this);
                progressDialog.setTitle(getString(R.string.please_wait));
                progressDialog.setMessage(getString(R.string.load_user_info));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                //extract
                if (email.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    email.setError(getString(R.string.email_error));
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    progressDialog.dismiss();
                    password.setError(getString(R.string.pass_error));
                    return;
                }


                //login
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                checkAccType();
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



    private void checkAccType() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child("Customer").orderByChild("email").equalTo(email.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = firebaseAuth.getCurrentUser();
                    progressDialog.dismiss();
                    if (user!=null && !user.isEmailVerified()){
                        notifyVerif();
                    }
                    startActivity(new Intent(getApplicationContext(), Homescreen.class));
                    finish();
                }else {
                    user = firebaseAuth.getCurrentUser();
                    progressDialog.dismiss();
                    if (user!=null && !user.isEmailVerified()){
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




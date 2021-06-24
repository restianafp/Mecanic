package com.vokasi.mecanic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vokasi.mecanic.R;

public class EmailVerification extends AppCompatActivity {

    private Button verifBtn;
    private ImageButton backBtn;
    private TextView title, desc;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);

        verifBtn = findViewById(R.id.btn_verif);
        backBtn = findViewById(R.id.backBtnVerif);
        title = findViewById(R.id.textView2);
        desc = findViewById(R.id.textView3);
        user = FirebaseAuth.getInstance().getCurrentUser();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        checkUserEmail();


    }

    private void checkUserEmail() {
        if (user.isEmailVerified()){
            verifBtn.setVisibility(View.GONE);
            title.setText("Email Terverifikasi");
            desc.setText("Email Anda telah terverifikasi oleh sistem");
        }else {
            sendVerifEmail();
        }
    }

    private void sendVerifEmail() {

        verifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                            verifBtn.setVisibility(View.GONE);
                            title.setText("Email Verifikasi Telah Dikirimkan");
                            desc.setText("Periksa email Anda untuk melakukan verifikasi");
                    }
                });
                Toast.makeText(EmailVerification.this,"Email telah dikirim",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
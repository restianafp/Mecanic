package com.vokasi.mecanic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vokasi.mecanic.R;

public class RegistMontir extends AppCompatActivity {

    protected EditText emailEt, passwordEt, confirmPassEt, firstNameEt;
    private TextView login;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_montir);


        login = findViewById(R.id.login);
        next = findViewById(R.id.btn_next);
        emailEt = findViewById(R.id.emailEtMont);
        passwordEt = findViewById(R.id.passwordEtMont);
        confirmPassEt = findViewById(R.id.confirmPassEtMont);
        firstNameEt = findViewById(R.id.firstNameEtMont);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backLogin = new Intent(RegistMontir.this, LoginPage.class);
                startActivity(backLogin);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDataAwal();
            }
        });
    }

    protected String  accType, email, password, confirmPassword, firstName;
    private void inputDataAwal() {


        accType = "Montir";
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = confirmPassEt.getText().toString().trim();
        firstName = firstNameEt.getText().toString().trim();

        //validate
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Format email tidak sesuai", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<8){
            Toast.makeText(this,"Password minimal 8 karakter", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!confirmPassword.equals(password)){
            Toast.makeText(this,"Password tidak sama", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(this,"Masukkan nama depan...", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent nextRegist = new Intent(RegistMontir.this, RegistMontirNext.class)
                .putExtra("data_Email", email)
                .putExtra("data_Pass", password)
                .putExtra("data_FirstName", firstName)
                .putExtra("data_accType", accType);
        startActivity(nextRegist);



    }
}
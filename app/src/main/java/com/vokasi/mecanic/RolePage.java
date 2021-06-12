package com.vokasi.mecanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RolePage extends AppCompatActivity {

    private TextView masuk;
    private Button cust, montir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_page);

        masuk = findViewById(R.id.masuk);
        cust = findViewById(R.id.btn_customer);
        montir = findViewById(R.id.btn_montir);

        cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoRegistcust = new Intent(RolePage.this, RegistCustomer.class);
                startActivity(gotoRegistcust);
            }
        });
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        montir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoRegistmontir = new Intent(RolePage.this, RegistMontir.class);
                startActivity(gotoRegistmontir);
            }
        });
    }
}
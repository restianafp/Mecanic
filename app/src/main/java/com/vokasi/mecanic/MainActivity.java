package com.vokasi.mecanic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public ImageView logoapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoapp=findViewById(R.id.logoapp);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent gotologin = new Intent(MainActivity.this, LoginPage.class);
                startActivity(gotologin);
                finish();
            }
        } ,2000);
    }
}
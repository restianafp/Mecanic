package com.vokasi.mecanic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vokasi.mecanic.R;
import com.vokasi.mecanic.model.UserCust;

public class RegistCustomer extends AppCompatActivity {

    private TextView masuk;
    private Button btnRegistCust;
    private EditText emailEt, passwordEt, confirmPassEt, firstNameEt, phoneNumEt;
    private FirebaseAuth firebaseAuth;
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID=0;
    private static final String CHANNEL_ID="ch1";
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_customer);


        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        confirmPassEt = findViewById(R.id.confirmPassEt);
        firstNameEt = findViewById(R.id.firstNameEt);
        phoneNumEt = findViewById(R.id.phoneNumEt);
        masuk = findViewById(R.id.login);
        btnRegistCust = findViewById(R.id.btn_regist_cust);

        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon tunggu");
        progressDialog.setCanceledOnTouchOutside(false);

        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        btnRegistCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputData();
            }
        });

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backLogin = new Intent(RegistCustomer.this, LoginPage.class);
                startActivity(backLogin);
            }
        });



    }






    private String accType, email, password, confirmPassword, firstName,phoneNum, photo_uri;
    private void inputData(){
        accType ="Customer";
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = confirmPassEt.getText().toString().trim();
        firstName = firstNameEt.getText().toString().trim();
        phoneNum = phoneNumEt.getText().toString().trim();
        photo_uri = "https://firebasestorage.googleapis.com/v0/b/mecanic-7161d.appspot.com/o/icon_nopic.png?alt=media&token=1c0d4374-837d-4f94-bf94-34110169ff85";

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
        if(TextUtils.isEmpty(phoneNum)){
            Toast.makeText(this,"Masukkan nomor WA...", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();

    }

    private void createAccount() {
        progressDialog.setMessage("Membuat akun...");
        progressDialog.show();

        //create account

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //berhasil dibuat
                        savedFirebaseData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //gagal dibuat
                progressDialog.dismiss();
                Toast.makeText(RegistCustomer.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savedFirebaseData() {
        progressDialog.setMessage("Menyimpan info akun...");
        String timestamp = ""+System.currentTimeMillis();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("Users");


       UserCust userData = new UserCust(accType, email, password, confirmPassword,
               firstName, phoneNum, photo_uri);
       mDatabase.child(accType).child(firebaseAuth.getUid()).setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               progressDialog.dismiss();
               notifySuccess();
               Intent loginSucces = new Intent(RegistCustomer.this, Homescreen.class);
               startActivity(loginSucces);

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progressDialog.dismiss();
               Toast.makeText(RegistCustomer.this,""+e.getMessage(),
                       Toast.LENGTH_SHORT).show();
           }
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
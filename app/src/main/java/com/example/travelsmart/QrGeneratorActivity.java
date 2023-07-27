package com.example.travelsmart;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.BitSet;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import java.util.UUID;

public class QrGeneratorActivity extends AppCompatActivity {
    TextView qrvalue,textViewTicketInfo;
    Button generateBtn;
    ImageView qrImage;
    UUID uuid =UUID.randomUUID();
    FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReferencef;
    public String Qrcodedata;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);
        qrvalue = findViewById(R.id.qrinput);
        generateBtn = findViewById(R.id.generateBtn);
        qrImage = findViewById(R.id.qrPlaceHolder);
        textViewTicketInfo=findViewById(R.id.textViewTicketInfo);

        authProfile =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =authProfile.getCurrentUser();




                Qrcodedata = uuid.toString();
                QRGEncoder qrgEncoder = new QRGEncoder(Qrcodedata, null, QRGContents.Type.TEXT,500);
                qrgEncoder.setColorBlack(Color.WHITE);
                qrgEncoder.setColorWhite(Color.BLACK);

                Bitmap qrBits = qrgEncoder.getBitmap();
                qrImage.setImageBitmap(qrBits);
                textViewTicketInfo.setText("Name :"+firebaseUser.getDisplayName()+"\nTicket ID: "+Qrcodedata+"\nEmail :"+firebaseUser.getEmail());

                generateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(QrGeneratorActivity.this,homePage.class);
                        startActivity(intent);
                        Qrcodedata = uuid.toString();
                        databaseReferencef= FirebaseDatabase.getInstance().getReference("Qrcode");
                        FirebaseAuth auth=FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser =auth.getCurrentUser();
                        ReadWriteQrCode writeQrCode =new ReadWriteQrCode(Qrcodedata);


                        DatabaseReference refrenceProfileQrcode = FirebaseDatabase.getInstance().getReference("Registered Users");
                        refrenceProfileQrcode.child(firebaseUser.getUid()).child("Qrcode").setValue(writeQrCode).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(QrGeneratorActivity.this, "QrcodeUploaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });





    }
}
package com.example.travelsmart;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.BitSet;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import java.util.UUID;

public class ViewTicketActivity extends AppCompatActivity {
    TextView qrvalue,textViewTicketInfo;
    Button generateBtn;
    UUID uuid =UUID.randomUUID();
    ImageView qrImage;
    FirebaseAuth authProfile;
    private String qrcodefetch;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        qrvalue = findViewById(R.id.qrinput);
        generateBtn = findViewById(R.id.generateBtn);
        qrImage = findViewById(R.id.qrPlaceHolder);
        textViewTicketInfo=findViewById(R.id.textViewTicketInfo);

        authProfile =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =authProfile.getCurrentUser();


//        DatabaseReference refrenceProfileQrcode = FirebaseDatabase.getInstance().getReference().child("Registered Users").child(firebaseUser.getUid()).child("Qrcode");
//        refrenceProfileQrcode.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ReadWriteQrCode readQrcode = snapshot.getValue((ReadWriteQrCode.class));
//
//                Toast.makeText(ViewTicketActivity.this, "working", Toast.LENGTH_SHORT).show();
//                qrcodefetch=readQrcode.Qrcode;
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(ViewTicketActivity.this, "wrong", Toast.LENGTH_SHORT).show();
//            }
//        });

        qrcodefetch= uuid.toString();
//        QRGEncoder qrgEncoder = new QRGEncoder(qrcodefetch, null, QRGContents.Type.TEXT,500);
//        qrgEncoder.setColorBlack(Color.WHITE);
//        qrgEncoder.setColorWhite(Color.BLACK);
//
//        Bitmap qrBits = qrgEncoder.getBitmap();
//        qrImage.setImageBitmap(qrBits);
        textViewTicketInfo.setText("Name :"+firebaseUser.getDisplayName()+"\nTicket ID: "+qrcodefetch+"\nEmail :"+firebaseUser.getEmail());

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ViewTicketActivity.this,homePage.class);
                startActivity(intent);

            }
        });





    }
}
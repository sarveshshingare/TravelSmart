package com.example.travelsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class homePage extends AppCompatActivity {
    private Button buttonBookTicket, buttonViewTicket, buttonTimeTable, buttonMetroMap,buttonGoogleMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        buttonBookTicket = findViewById(R.id.Button_BookTicket);
        buttonViewTicket = findViewById(R.id.Button_ViewTicket);
        buttonTimeTable = findViewById(R.id.Button_TimeTable);
        buttonMetroMap = findViewById(R.id.Button_MetroMap);
        buttonGoogleMaps=findViewById(R.id.Button_GoogleMaps);


        buttonBookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePage.this, BookTicketActivity.class);
                startActivity(intent);
            }
        });

        buttonMetroMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePage.this, MetroMapActivity.class);
                startActivity(intent);
            }
        });
        buttonTimeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePage.this, TimeTableActivity.class);
                startActivity(intent);
            }
        });
        buttonViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePage.this, ViewTicketActivity.class);
                startActivity(intent);
            }
        });

        buttonGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homePage.this, MapsActivity .class);
                startActivity(intent);
            }
        });
    }

    //create action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu items
        getMenuInflater().inflate(R.menu.homepage_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.manage_profile) {
            //refresh activity
            Intent intent = new Intent(homePage.this, UserProfileActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else {
            Toast.makeText(homePage.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);

    }

}

package com.example.mils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registration_Service_provider_catalog extends AppCompatActivity {

    private Button driver_button,vehicle_button;
    private String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__service_provider_catalog);

        driver_button = findViewById(R.id.driver_registration_button);
        vehicle_button = findViewById(R.id.vehicle_registration_button);

        //domain = getIntent().getExtras().get("Domain").toString();

        driver_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Registration_Service_provider_catalog.this,Registration.class);
               // intent.putExtra("Domain",domain);
                intent.putExtra("Domain","Driver Job");
                startActivity(intent);
            }
        });

        vehicle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration_Service_provider_catalog.this,Registration.class);
               // intent.putExtra("Domain",domain);
                intent.putExtra("Domain","Vehicle Owner");
                startActivity(intent);
            }
        });

    }
}

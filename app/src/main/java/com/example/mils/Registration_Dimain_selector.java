package com.example.mils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Registration_Dimain_selector extends AppCompatActivity {

    private Button industry_button,service_provider_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration__dimain_selector);

        industry_button = findViewById(R.id.industry_button);
        service_provider_button = findViewById(R.id.service_provider_button);

        industry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration_Dimain_selector.this,Registration.class);
                intent.putExtra("Domain","Industry");
                startActivity(intent);
            }
        });

        service_provider_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration_Dimain_selector.this,Registration_Service_provider_catalog.class);
               // intent.putExtra("Domain","Service Provider");
                startActivity(intent);
            }
        });
    }
}

package com.example.mils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.Objects;

public class Registration extends AppCompatActivity {


    private EditText reg_name, reg_phone, reg_password;
    private Button next;
    private String domain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

         setupUiId();

        // domain = (getIntent().getExtras().get("Domain")).toString();
         domain = getIntent().getExtras().get("Domain").toString();

         next.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 createAccount();
             }
         });


    }


    private void setupUiId() {

        reg_name = findViewById(R.id.name_reg);
        reg_phone = findViewById(R.id.mobile_number_reg);
        reg_password = findViewById(R.id.password_reg);

        next = findViewById(R.id.next_reg_button);

    }

    private void createAccount() {

        String name =  reg_name.getText().toString().trim();
        String phone = reg_phone.getText().toString().trim();
        String password = reg_password.getText().toString().trim();

        if(name.isEmpty() || phone.isEmpty() || password.isEmpty()){
            Toast.makeText(Registration.this,"Please fill all the details...",Toast.LENGTH_SHORT).show();
        }else if(domain.equals("Driver Job")){
            Intent intent = new Intent(Registration.this,Driving_license.class);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("password",password);
            intent.putExtra("Domain",domain);
            //intent.putExtra("Sub-Domain",domain);
            startActivity(intent);
            finish();
        }else if(domain.equals("Vehicle Owner")){
            Intent intent = new Intent(Registration.this,Vehicle_information.class);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("password",password);
            intent.putExtra("Domain",domain);
           // intent.putExtra("Sub-Domain",domain);
            startActivity(intent);
            finish();
        }else if(domain.equals("Industry")){
            Intent intent = new Intent(Registration.this,Industry_information.class);
            intent.putExtra("name",name);
            intent.putExtra("phone",phone);
            intent.putExtra("password",password);
            intent.putExtra("Domain",domain);
            // intent.putExtra("Sub-Domain",domain);
            startActivity(intent);
            finish();
        }

    }


}

/*
if(domain.equals("Driver Job")){
        Intent intent = new Intent(Registration.this,Driving_license.class);
        intent.putExtra("name",name);
        intent.putExtra("phone",phone);
        intent.putExtra("password",password);
        intent.putExtra("Domain",domain);
        intent.putExtra("Sub-Domain",sub_domain);
        startActivity(intent);
        finish();
        }else if(domain.equals("Vehicle Owner")){
        Intent intent = new Intent(Registration.this,Vehicle_information.class);
        intent.putExtra("name",name);
        intent.putExtra("phone",phone);
        intent.putExtra("password",password);
        intent.putExtra("Domain",domain);
        intent.putExtra("Sub-Domain",sub_domain);
        startActivity(intent);
        finish();
        }

 */
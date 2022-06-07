package com.example.mils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Driving_license extends AppCompatActivity {

    private Button validity_button, dob_button;
    private EditText license_number;
    private ImageView license_image;
    private Button next_drive;

    private DatabaseReference registration_ref;
    private DatabaseReference license_ref;
    private StorageReference driving_license_ref;

    private ProgressDialog loading_bar;

    private static final int Gallery_Pic = 1;
    private Uri ImageUri;

    private String license_Number, save_current_date, Save_current_time, product_random_key, downloadImageUrl;
    private String Name, Phone, Password, Domain;
    private String todays_date;
    private String validity_date, birth_date,age;

    private int day, month, year;
    private int dob_year, validity_year;
    private int on_date,age_diffrence;
    Calendar c;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_license);

        setUpId();



        Name = getIntent().getExtras().get("name").toString();
        Phone = getIntent().getExtras().get("phone").toString();
        Password = getIntent().getExtras().get("password").toString();
        Domain = getIntent().getExtras().get("Domain").toString();



        registration_ref = FirebaseDatabase.getInstance().getReference();
        license_ref = FirebaseDatabase.getInstance().getReference();
        driving_license_ref = FirebaseStorage.getInstance().getReference().child("Users_Images_data");


        validity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd = new DatePickerDialog(Driving_license.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int myear, int mmonth, int mday) {
                        validity_year = myear;
                        validity_date = mday + "/" + (mmonth + 1) + "/" + myear;
                        validity_button.setText(mday + "/" + (mmonth + 1) + "/" + myear);
                    }
                }, day, month, year);
                dpd.show();
            }
        });

        dob_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dpd = new DatePickerDialog(Driving_license.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int nyear, int nmonth, int nday) {
                        dob_year = nyear;
                        birth_date = nday + "/" + (nmonth + 1) + "/" + nyear;
                        dob_button.setText(nday + "/" + (nmonth + 1) + "/" + nyear);
                    }
                }, day, month, year);
                dpd.show();
                ;
            }
        });


        license_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        next_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //validateLicenseData();

                SimpleDateFormat t_date = new SimpleDateFormat("yyyy");
                todays_date = t_date.format(c.getTime());
                on_date = Integer.parseInt(todays_date);
                age_diffrence = on_date - dob_year;
                age = Integer.toString(age_diffrence);
                license_number.setText(age);
            }
        });


    }

    private void openGallery() {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, Gallery_Pic);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pic && resultCode == RESULT_OK && data != null) {

            ImageUri = data.getData();
            license_image.setImageURI(ImageUri);
        }
    }


    private void validateLicenseData() {

        license_Number = license_number.getText().toString().trim();

        if (ImageUri == null) {
            Toast.makeText(Driving_license.this, "Driving license image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (license_Number.isEmpty()) {
            Toast.makeText(Driving_license.this, "Please fill the driving license number...", Toast.LENGTH_SHORT).show();
        } else if (dob_year == 0) {
            Toast.makeText(Driving_license.this, "Select Birth date....", Toast.LENGTH_SHORT).show();
        } else if (validity_year == 0) {
            Toast.makeText(Driving_license.this, "Select license validity date...", Toast.LENGTH_SHORT).show();
        } else {
                storeDrivingLicenseInfo();
        }

    }



    private void storeDrivingLicenseInfo() {

               loading_bar.setTitle("Adding Driving License");
               loading_bar.setMessage("Please wait, while we are adding the driving license.");
               loading_bar.setCanceledOnTouchOutside(false);
               loading_bar.show();

               Calendar calendar = Calendar.getInstance();

        SimpleDateFormat current_date = new SimpleDateFormat("MMM/dd/yyyy");
        save_current_date = current_date.format(calendar.getTime());

       // SimpleDateFormat t_date = new SimpleDateFormat("yyyy");
        //todays_date = t_date.format(c.getTime());

        SimpleDateFormat current_time = new SimpleDateFormat("HH:mm:ss a");
        Save_current_time = current_time.format(calendar.getTime());

        //on_date = Integer.parseInt(todays_date);

        //int age_diffrence = on_date - dob_year;

        product_random_key = Phone + "_" +save_current_date + "_" + Save_current_time  ;

        final StorageReference file_path = driving_license_ref.child(ImageUri.getLastPathSegment() + product_random_key +".jpg");
        final UploadTask uploadTask = file_path.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(Driving_license.this,"Error: " + message,Toast.LENGTH_SHORT).show();
                loading_bar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Driving_license.this,"Driving license uploaded successfully",Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(task.isSuccessful()){

                            downloadImageUrl = file_path.getDownloadUrl().toString();
                        }
                        return file_path.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(Driving_license.this,"Driving license save to Database Successfully.",Toast.LENGTH_SHORT).show();
                           // SaveInfoToDatabase();
                        }
                    }
                });

            }
        });
    }
/*
    private void SaveInfoToDatabase() {

        registration_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(Domain).child(Phone).exists())){
                    HashMap<String,Object> usersmap = new HashMap<>();
                    usersmap.put("Name",Name);
                    usersmap.put("Phone_Number",Phone);
                    usersmap.put("Password",Password);
                    usersmap.put("Date",save_current_date);
                    usersmap.put("Time",getSave_current_time);

                    registration_ref.child("Users").child(Domain).child(Phone).updateChildren(usersmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                loading_bar.dismiss();
                                startActivity(new Intent(Driving_license.this,Personal_Info.class));
/*
                                HashMap<String,Object> licensemap = new HashMap<>();
                                licensemap.put("License_Number",license_Number);
                                licensemap.put("Image",downloadImageUrl);
                                licensemap.put("Validity_Date",validity_date);
                                licensemap.put("Birth_Date",birth_date);
                                licensemap.put("Phone_Number",Phone);
                                licensemap.put("Domain",Domain);

                                license_ref.child(Phone).updateChildren(licensemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            loading_bar.dismiss();
                                            Toast.makeText(Driving_license.this,"Congratulations, your account has been created",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Driving_license.this,Personal_Info.class));
                                            finish();

                                        }else{
                                            loading_bar.dismiss();
                                            String message = task.getException().toString();
                                            Toast.makeText(Driving_license.this,"Error: " + message,Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }else{
                                Toast.makeText(Driving_license.this,"This" + Phone +" already exists.",Toast.LENGTH_SHORT).show();
                                loading_bar.dismiss();
                                Toast.makeText(Driving_license.this,"Please try again by using another phone number", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(Driving_license.this,MainActivity.class));
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
*/
    private void setUpId() {

        validity_button = findViewById(R.id.validity_date_Button);
        dob_button = findViewById(R.id.dob_Button);
        next_drive = findViewById(R.id.next_button);

        license_number = findViewById(R.id.driving_license_number);

        license_image = findViewById(R.id.driver_license_image);

        c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
    }
}


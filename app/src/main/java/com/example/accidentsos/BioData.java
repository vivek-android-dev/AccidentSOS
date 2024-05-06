package com.example.accidentsos;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BioData extends AppCompatActivity {

    private String uid,uname,unum,uage,ublg,uloc,umail,ugender;

    private EditText name,num,age,mail,loc,gender,blood;

    ImageView button;

    Button update;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bio_data);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

            name = findViewById(R.id.nameed);
            num = findViewById(R.id.mobileed);
            age = findViewById(R.id.ageed);
            blood = findViewById(R.id.blooded);
            mail = findViewById(R.id.emailed);
            loc = findViewById(R.id.locationed);
            gender = findViewById(R.id.gendered);
            button = findViewById(R.id.btn);
            update = findViewById(R.id.getStart);

            SharedPreferences bio = getSharedPreferences("usersf",MODE_PRIVATE);
            uid = bio.getString("id",null);
            uname = bio.getString("name",null);
            unum = bio.getString("number",null);
            uage = bio.getString("age",null);
            ublg = bio.getString("bloodgroup",null);
            uloc = bio.getString("location",null);
            umail = bio.getString("email",null);
            ugender = bio.getString("gender",null);


            name.setText(uname);
            num.setText(unum);
            age.setText(uage);
            blood.setText(ublg);
            mail.setText(umail);
            loc.setText(uloc);
            gender.setText(ugender);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validateFields()){

                    }
                }
            });

    }
    private boolean validateFields() {
        boolean isValid = true;

        // Validate Name field
        String nameText = name.getText().toString().trim();
        if (TextUtils.isEmpty(nameText)) {
            name.setError("Name is required");
            isValid = false;
        }

        // Validate Number field
        String numText = num.getText().toString().trim();
        if (TextUtils.isEmpty(numText)||TextUtils.isDigitsOnly(numText)|| numText.length()!=2) {
            num.setError("Mobile number is required");
            isValid = false;
        }

        // Validate Age field
        String ageText = age.getText().toString().trim();
        if (TextUtils.isEmpty(ageText)) {
            age.setError("Age is required");
            isValid = false;
        }

        // Validate Blood Group field
        String bloodText = blood.getText().toString().trim();
        if (TextUtils.isEmpty(bloodText)) {
            blood.setError("Blood group is required");
            isValid = false;
        }

        // Validate Email field
        String mailText = mail.getText().toString().trim();
        if (TextUtils.isEmpty(mailText)|| !Patterns.EMAIL_ADDRESS.matcher(mailText).matches()) {
            mail.setError("Email is required");
            isValid = false;
        }

        // Validate Location field
        String locText = loc.getText().toString().trim();
        if (TextUtils.isEmpty(locText)) {
            loc.setError("Location is required");
            isValid = false;
        }

        // Validate Gender field
        String genderText = gender.getText().toString().trim();
        if (TextUtils.isEmpty(genderText)) {
            gender.setError("Gender is required");
            isValid = false;
        }

        return isValid;
    }
}
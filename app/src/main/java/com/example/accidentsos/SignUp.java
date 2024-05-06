package com.example.accidentsos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.accidentsos.ServerResponses.CommonResponse;
import com.example.accidentsos.api.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    EditText name,username,age,mobileNo,bloodgrp,password,gender,email;
    Button signUp;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        name =findViewById(R.id.name);
        username =findViewById(R.id.username);
        age =findViewById(R.id.age);
        mobileNo =findViewById(R.id.mobile);
        bloodgrp =findViewById(R.id.bloodgrp);
        password =findViewById(R.id.password);
        gender =findViewById(R.id.gender);
        email =findViewById(R.id.email);
        signUp = findViewById(R.id.signIn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()) {

                    Call<CommonResponse> callResponse = RestClient.makeApi().signUp(name.getText().toString(), age.getText().toString(), gender.getText().toString(),
                            bloodgrp.getText().toString(), email.getText().toString(), mobileNo.getText().toString(), username.getText().toString(), password.getText().toString());

                    callResponse.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().equals("200")) {
                                    Toast.makeText(SignUp.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUp.this, SignIn.class));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Toast.makeText(SignUp.this, "check internet" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }
    private boolean validateFields() {
        // Get text from EditText fields
        String nameText = name.getText().toString().trim();
        String usernameText = username.getText().toString().trim();
        String ageText = age.getText().toString().trim();
        String mobileNoText = mobileNo.getText().toString().trim();
        String bloodgrpText = bloodgrp.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        String genderText = gender.getText().toString().trim();
        String emailText = email.getText().toString().trim();

        // Validate name
        if (TextUtils.isEmpty(nameText)) {
            name.setError("Please enter name");
            return false;
        }



        // Validate username
        if (TextUtils.isEmpty(usernameText)) {
            username.setError("Please enter username");
            return false;
        }

        // Validate age (check if it's a valid integer)
        if (TextUtils.isEmpty(ageText) || !TextUtils.isDigitsOnly(ageText)) {
            age.setError("Please enter a valid age");
            return false;
        }

        if (TextUtils.isEmpty(genderText)) {
            gender.setError("Please enter gender");
            return false;
        }

        // Validate mobile number (check if it's a valid 10-digit number)
        if (TextUtils.isEmpty(mobileNoText) || mobileNoText.length() != 10 || !TextUtils.isDigitsOnly(mobileNoText)) {
            mobileNo.setError("Please enter a valid 10-digit mobile number");
            return false;
        }

        if (TextUtils.isEmpty(bloodgrpText)) {
            bloodgrp.setError("Please enter blood group");
            return false;
        }

        // Validate blood group (optional)
        // You can add specific validation rules for blood group if needed

        // Validate password
        if (TextUtils.isEmpty(passwordText)) {
            password.setError("Please enter password");
            return false;
        }

        // Validate gender (optional)
        // You can add specific validation rules for gender if needed

        // Validate email format
        if (TextUtils.isEmpty(emailText) || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        }

        // All fields pass validation
        return true;
    }
}
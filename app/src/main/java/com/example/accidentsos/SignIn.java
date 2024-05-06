package com.example.accidentsos;

import static com.example.accidentsos.AppConfig.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.accidentsos.ServerResponses.SignInResponse;
import com.example.accidentsos.api.RestClient;
import com.google.android.material.badge.ExperimentalBadgeUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@ExperimentalBadgeUtils public class SignIn extends AppCompatActivity {

    private TextView signUp;
    private Button signIn;

    private EditText username,password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        signUp = findViewById(R.id.signUp);
        signIn = findViewById(R.id.signInBtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        SharedPreferences sign = getSharedPreferences("sign",MODE_PRIVATE);
        String status=sign.getString("status",null);

        Log.d(TAG, "onCreate: "+status);
        if(status!=null){
        if(status.equals("signed")){
            startActivity(new Intent(this,NavigationBar.class));
            finish();
        }}

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation()) {
                    Call<SignInResponse> sign = RestClient.makeApi().signIn(username.getText().toString(), password.getText().toString());
                    sign.enqueue(new Callback<SignInResponse>() {
                        @Override
                        public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().equals("200")) {
                                    SharedPreferences sf = getSharedPreferences("usersf", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sf.edit();
                                    editor.putString("id", response.body().getId());
                                    editor.putString("name", response.body().getName());
                                    editor.putString("number", response.body().getNumber());
                                    editor.putString("email", response.body().getEmail());
                                    editor.putString("age", response.body().getAge());
                                    editor.putString("bloodgroup", response.body().getBloodgroup());
                                    editor.putString("location", response.body().getAddress());
                                    editor.putString("gender", response.body().getGender());
                                    editor.apply();
                                    SharedPreferences signInsf = getSharedPreferences("sign", MODE_PRIVATE);
                                    SharedPreferences.Editor edit = signInsf.edit();
                                    edit.putString("status", "signed");
                                    edit.apply();
                                    Toast.makeText(SignIn.this, "SignIn successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignIn.this, NavigationBar.class);
                                    startActivity(intent);
                                    finish();
                                }
                                if (response.body().getStatus().equals("404")) {
                                    Toast.makeText(SignIn.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                                }

                            }
                            Log.d(TAG, "onResponse: " + response.message());

                        }

                        @Override
                        public void onFailure(Call<SignInResponse> call, Throwable t) {
                            Toast.makeText(SignIn.this, " check internet\t" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: " + t.getMessage());
                        }
                    });

                }
            }
        });


    }

    private boolean validation() {
        boolean isValid = true; // Flag to track validation status

        if (username.getText().toString().isEmpty()) {
            username.setError("User name required");
            isValid = false; // Set flag to false if validation fails
        }

        if (password.getText().toString().isEmpty()) {
            password.setError("Password required");
            isValid = false;
        }

        return isValid; // Return validation status
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}
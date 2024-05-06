package com.example.accidentsos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.accidentsos.ServerResponses.CommonResponse;
import com.example.accidentsos.api.RestClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBack extends AppCompatActivity {

    private Button satisfied,unsatisfied,none,send;
    TextInputEditText description;
    String feedback="NONE";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feed_back);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sf =getSharedPreferences("usersf",MODE_PRIVATE);
        id= sf.getString("id",null);

        satisfied = findViewById(R.id.satisfiedbtn);
        unsatisfied = findViewById(R.id.unsatisfiedbtn);
        none = findViewById(R.id.nonebtn);
        send = findViewById(R.id.sendbtn);
        description=findViewById(R.id.description);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        satisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    feedback= satisfied.getText().toString();
            }
        });

        unsatisfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback= unsatisfied.getText().toString();
            }
        });

        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback= none.getText().toString();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation()) {
                    Call<CommonResponse> commonResponseCall = RestClient.makeApi().feedback(id, feedback, description.getText().toString());
                    commonResponseCall.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().equals("200")) {
                                    Toast.makeText(FeedBack.this, "feedback send", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {
                            Toast.makeText(FeedBack.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private boolean validation() {
        String descriptionText = description.getText().toString().trim();

        if (descriptionText.isEmpty()) {
            description.setError("Please provide feedback");
            return false;
        }

        return true;
    }
}
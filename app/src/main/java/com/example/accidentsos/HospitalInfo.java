package com.example.accidentsos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class HospitalInfo extends AppCompatActivity {

    String pid, pname, uid, uname, hname, hAddress, unum;

    TextInputEditText hName, attName, attNum, ptid, attid;

    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hospital_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hName = findViewById(R.id.hName);
        attName = findViewById(R.id.attName);
        attNum = findViewById(R.id.attendernumber);
        ptid = findViewById(R.id.patientid);
        attid = findViewById(R.id.attid);
        button = findViewById(R.id.done);

        SharedPreferences sf = getSharedPreferences("notificationsf", Context.MODE_PRIVATE);

        pid = sf.getString("id", null);
        pname = sf.getString("name", null);

        SharedPreferences usf = getSharedPreferences("usersf", Context.MODE_PRIVATE);
        uid = usf.getString("id", null);
        uname = usf.getString("name", null);
        unum = usf.getString("number", null);

        SharedPreferences hsf = getSharedPreferences("Hospitalsf", Context.MODE_PRIVATE);
        hname = hsf.getString("hName", null);
        hAddress = hsf.getString("address", null);

        hName.setText(hname);
        ptid.setText(pid);
        attName.setText(uname);
        attid.setText(uid);
        attNum.setText(unum);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    Call<CommonResponse> res = RestClient.makeApi().hospitalInfo(hname, uname, unum, pid, uid);
                    res.enqueue(new Callback<CommonResponse>() {
                        @Override
                        public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus().equals("200")) {
                                    Toast.makeText(HospitalInfo.this, "Thank you! successfully completed", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(HospitalInfo.this, NavigationBar.class));
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<CommonResponse> call, Throwable t) {

                        }
                    });

                }
            }
        });


    }

    private boolean validation() {
        boolean isValid = true; // Flag to track validation status

        if (hName.getText().toString().isEmpty()) {
            hName.setError("Can't leave empty");
            isValid = false; // Set flag to false if validation fails
        }

        if (ptid.getText().toString().isEmpty()) {
            ptid.setError("Can't leave empty");
            isValid = false;
        }

        if (attName.getText().toString().isEmpty()) {
            attName.setError("Can't leave empty");
            isValid = false;
        }

        if (attid.getText().toString().isEmpty()) {
            attid.setError("Can't leave empty");
            isValid = false;
        }

        if (attNum.getText().toString().isEmpty()) {
            attNum.setError("Can't leave empty");
            isValid = false;
        }

        return isValid; // Return validation status
    }
}
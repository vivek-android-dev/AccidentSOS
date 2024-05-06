package com.example.accidentsos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Options extends AppCompatActivity {

    private ImageView editimg, historyimg, feedbackimg, friendsimg, terminateimg, logoutimg, backbtn;

    private History history = new History();
    private Friends friends = new Friends();
    private TextView mobile, name;

    private NavigationBar navigationBar = new NavigationBar();
    private String uid, uname, unum;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_options);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editimg = findViewById(R.id.editbtn);
        historyimg = findViewById(R.id.historyid);
        feedbackimg = findViewById(R.id.feedbackid);
        friendsimg = findViewById(R.id.friendsid);
        terminateimg = findViewById(R.id.profile);
        logoutimg = findViewById(R.id.logout);
        backbtn = findViewById(R.id.backBtn);
        mobile = findViewById(R.id.mobile);
        name = findViewById(R.id.name);

        SharedPreferences usf = getSharedPreferences("usersf", Context.MODE_PRIVATE);
        uid = usf.getString("id", null);
        uname = usf.getString("name", null);
        unum = usf.getString("number", null);

        if (name != null)
            name.setText(uname);

        if (mobile != null)
            mobile.setText(unum);

        editimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Options.this, BioData.class));
            }
        });

        historyimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                navigationBar.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,history).commit();

                startActivity(new Intent(Options.this, NavigationBar.class));

            }
        });

        feedbackimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Options.this, FeedBack.class));
            }
        });
        friendsimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,friends).commit();
                startActivity(new Intent(Options.this, NavigationBar.class));
            }
        });
        terminateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(Options.this)

                        .setTitle("Account")
                        .setMessage("Are you sure you want to Terminate?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences signInsf = getSharedPreferences("sign", MODE_PRIVATE);
                                SharedPreferences.Editor edit = signInsf.edit();
                                edit.putString("status", "signedout");
                                edit.apply();

                                startActivity(new Intent(Options.this, SignIn.class));
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });
        logoutimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new AlertDialog.Builder(Options.this)
                        .setTitle("Account")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences signInsf = getSharedPreferences("sign", MODE_PRIVATE);
                                SharedPreferences.Editor edit = signInsf.edit();
                                edit.putString("status", "signedout");
                                edit.apply();
                                startActivity(new Intent(Options.this, SignIn.class));
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
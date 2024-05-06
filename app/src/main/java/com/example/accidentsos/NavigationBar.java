package com.example.accidentsos;

import static com.example.accidentsos.AppConfig.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.accidentsos.ServerResponses.FriendsResponse;
import com.example.accidentsos.api.RestClient;
import com.example.accidentsos.databinding.ActivityMainBinding;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ExperimentalBadgeUtils public class NavigationBar extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    Home home = new Home();
    Friends friends = new Friends();
    History history = new History();


//    ImageView backbtn,notificationbtn,optionbtn;

    Toolbar toolbar;

    int pendingSMSCount = 1;
    ActivityMainBinding binding;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_navigation_bar);
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        toolbar = findViewById(R.id.toolBar);


        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Call<FriendsResponse> friendsResponseCall = RestClient.makeApi().friends();

        friendsResponseCall.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            pendingSMSCount =response.body().getData().size();
                            invalidateOptionsMenu();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendsResponse> call, Throwable t) {

            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,home).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int menu=menuItem.getItemId();
                if(menu==R.id.homeid){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,home).commit();
                }
                if(menu==R.id.friendsid){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,friends).commit();
                }
                if(menu==R.id.historyid){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,history).commit();
                }
                return true;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sign = getSharedPreferences("sign",MODE_PRIVATE);
        String status=sign.getString("status",null);

        Log.d(TAG, "onCreateNav: "+status);
        if(status!=null){
            if(status.equals("signedout")){
                finish();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        SharedPreferences sign = getSharedPreferences("sign",MODE_PRIVATE);
        String status=sign.getString("status",null);

        Log.d(TAG, "onCreateNav: "+status);
        if(status!=null){
            if(status.equals("signedout")){
                finish();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Check which menu item was clicked and show toast accordingly
        if (id == R.id.notifications) {
            // Reset pendingSMSCount to zero
            pendingSMSCount = 0;
            // Update badge count
            invalidateOptionsMenu();
            startActivity(new Intent(NavigationBar.this,Notifications.class));

            return true;
        } else if (id == R.id.options) {

            startActivity(new Intent(NavigationBar.this,Options.class));
            return true;
        } else if (id == R.id.logout) {

            SharedPreferences signInsf = getSharedPreferences("sign", MODE_PRIVATE);
            SharedPreferences.Editor edit = signInsf.edit();
            edit.putString("status", "signedout");
            edit.apply();
            startActivity(new Intent(NavigationBar.this,SignIn.class));
            return true;
        } else if (id == R.id.exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.notifications);

        if (menuItem != null) {
            View view = menuItem.getActionView();

            if (view != null) {
                TextView badgeCount = view.findViewById(R.id.notification_badge);

                if (badgeCount != null) {
                    if (pendingSMSCount == 0) {
                        if (badgeCount.getVisibility() != View.GONE) {
                            badgeCount.setVisibility(View.GONE);
                        }
                    } else {
                        badgeCount.setText(String.valueOf(Math.min(pendingSMSCount, 99)));
                        if (badgeCount.getVisibility() != View.VISIBLE) {
                            badgeCount.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(menuItem);
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

}
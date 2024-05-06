package com.example.accidentsos;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accidentsos.DataModel.NotificationModel;
import com.example.accidentsos.ServerResponses.FriendsResponse;
import com.example.accidentsos.adapter.NotificationAdapter;
import com.example.accidentsos.api.RestClient;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notifications extends AppCompatActivity {

    NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<NotificationModel> data=new ArrayList<>();

    ImageView bbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);



        recyclerView = findViewById(R.id.notificationRecyclerview);
        bbtn = findViewById(R.id.backBtn);

        bbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Call<FriendsResponse> friendsResponseCall = RestClient.makeApi().friends();

        friendsResponseCall.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){

                        int badgeCount = response.body().getData().size(); // Use appropriate count
                        ShortcutBadger.applyCount(Notifications.this, badgeCount);

                        for (int i = 0; i < response.body().getData().size(); i++) {
                            FriendsResponse.Datum d = response.body().getData().get(i);
                            data.add(new NotificationModel(d.getId(),d.getName(),d.getAge(),d.getDob(),d.getGender(),d.getBloodgroup(),d.getMobilenumber(),d.getLongitude(),d.getLatitude(),d.getAddress()));
                        }


                        adapter = new NotificationAdapter(Notifications.this,data);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendsResponse> call, Throwable t) {

            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        ShortcutBadger.removeCount(Notifications.this);
    }
}
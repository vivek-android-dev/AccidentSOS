package com.example.accidentsos;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accidentsos.DataModel.FriendsModel;
import com.example.accidentsos.ServerResponses.FriendsResponse;
import com.example.accidentsos.adapter.FriendsAdapter;
import com.example.accidentsos.api.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Friends extends Fragment {

    FriendsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<FriendsModel> data=new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friends, container, false);

        recyclerView = view.findViewById(R.id.friendsRecyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences sign = getActivity().getSharedPreferences("sign",MODE_PRIVATE);
        String status=sign.getString("status",null);

        Log.d("Friends", "onCreate: "+status);
        if(status!=null){
            if(status.equals("signedout")){
                getActivity().finish();
            }}

        Call<FriendsResponse> friendsResponseCall = RestClient.makeApi().friends();

        friendsResponseCall.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){
                        data.clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            FriendsResponse.Datum d = response.body().getData().get(i);
                            data.add(new FriendsModel(d.getId(),d.getName(),d.getAge(),d.getDob(),d.getGender(),d.getBloodgroup(),d.getMobilenumber(),d.getLongitude(),d.getLatitude(),d.getAddress()));
                        }

                        if(getContext()!=null)
                        { adapter = new FriendsAdapter(getContext(),data);}
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<FriendsResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
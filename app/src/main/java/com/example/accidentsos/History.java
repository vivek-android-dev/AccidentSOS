package com.example.accidentsos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accidentsos.DataModel.HistoryModel;
import com.example.accidentsos.ServerResponses.HistoryResponse;
import com.example.accidentsos.adapter.HistoryAdapter;
import com.example.accidentsos.api.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class History extends Fragment {


    HistoryAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<HistoryModel> data=new ArrayList<>();
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.historyRecyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences usf = getActivity().getSharedPreferences("usersf", Context.MODE_PRIVATE);
        uid = usf.getString("id",null);

        Call<HistoryResponse> historyModelCall = RestClient.makeApi().history(uid);

        historyModelCall.enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus().equals("200")){
                        data.clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            HistoryResponse.Datum d = response.body().getData().get(i);
                            data.add(new HistoryModel(d.getHospital_name(),d.getPatient_name(),d.getAttender_name(),d.getDate()));
                        }

                        if(getContext()!=null)
                        { adapter = new HistoryAdapter(getContext(),data);}
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;


    }
}
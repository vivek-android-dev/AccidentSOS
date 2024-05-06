package com.example.accidentsos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accidentsos.DataModel.HistoryModel;
import com.example.accidentsos.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context mcontext;
    private ArrayList<HistoryModel> mData;
    public HistoryAdapter(Context context, ArrayList<HistoryModel> data) {

        this.mcontext = LayoutInflater.from(context).getContext();
        this.mData = data;
    }
    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_cardview, parent, false);

        return new HistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        HistoryModel history =mData.get(position);
        String hospitalname = history.getHospital_name();
        String patientname = history.getPatient_name();
        String attendername = history.getAttender_name();
        String accdate = history.getDate();
        String string= attendername +" "+
                "thanks for helping the person named has " + patientname + "." +" he is admited in the " + hospitalname +
                " hospital on " + accdate+ " correct time you can get reward from the hospital Rupees 5000 from the hospital";

        holder.history.setText(string);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{
        TextView history;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            history= itemView.findViewById(R.id.historycard);
        }
    }
}

package com.example.accidentsos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accidentsos.DataModel.FriendsModel;
import com.example.accidentsos.R;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {

    Context mcontext;
    private List<FriendsModel> mData;

    public FriendsAdapter(Context context, List<FriendsModel> data) {

        this.mcontext = LayoutInflater.from(context).getContext();
        this.mData = data;
    }

    @NonNull
    @Override
    public FriendsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_cardview, parent, false);
        return new FriendsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.MyViewHolder holder, int position) {

        FriendsModel friendsModel = mData.get(position);
        holder.textView.setText(friendsModel.getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mcontext)
                        .setTitle("Details")
                        .setMessage("Name: " + friendsModel.getName() + "\n\n" + "Mobile No: " + friendsModel.getMobilenumber() + "\n\n" + "Blood Group: " + friendsModel.getBloodgroup())
                        .setIcon(R.drawable.img)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.nameed);

        }
    }
}

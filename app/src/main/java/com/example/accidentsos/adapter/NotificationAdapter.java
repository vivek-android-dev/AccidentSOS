package com.example.accidentsos.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accidentsos.MapsActivity;
import com.example.accidentsos.DataModel.NotificationModel;
import com.example.accidentsos.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context mcontext;
    private List<NotificationModel> mData;
    public NotificationAdapter(Context context, List<NotificationModel> data) {
        this.mcontext = LayoutInflater.from(context).getContext();
        this.mData = data;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_cardview, parent, false);
        return new NotificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        NotificationModel notificationModel =mData.get(position);
        holder.textView.setText(notificationModel.getName());
        holder.textView2.setText(notificationModel.getAddress());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MapsActivity.class);
                SharedPreferences sf = mcontext.getSharedPreferences("notificationsf",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("id", notificationModel.getId());
                editor.putString("name", notificationModel.getName());
                editor.putString("age", notificationModel.getAge());
                editor.putString("dob", notificationModel.getDob());
                editor.putString("address", notificationModel.getAddress());
                editor.putString("gender", notificationModel.getGender());
                editor.putString("latitude", notificationModel.getLatitude());
                editor.putString("longitude", notificationModel.getLongitude());
                editor.putString("mobile", notificationModel.getMobilenumber());
                editor.putString("blood", notificationModel.getBloodgroup());
                editor.apply();
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView,textView2;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.nameed);
            textView2 = itemView.findViewById(R.id.address);
            cardView = itemView.findViewById(R.id.notificationCardView);
        }
    }
}

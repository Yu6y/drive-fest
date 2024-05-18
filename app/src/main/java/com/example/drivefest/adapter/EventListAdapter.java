package com.example.drivefest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drivefest.data.model.EventShort;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivefest.R;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListViewHolder>{
    private Context context;
    private List<EventShort> list;

    public EventListAdapter(Context context, List<EventShort> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventListViewHolder(LayoutInflater.from(context).inflate(R.layout.event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventListViewHolder holder, int position) {
        holder.eventTitle.setText(list.get(position).getName());
        holder.eventDate.setText(list.get(position).getDate().toString());
        holder.eventCity.setText(list.get(position).getLocation());
        holder.eventFollowers.setText("Obserwujących: " + list.get(position).getFollowersCount());
        Glide.with(context).load(list.get(position).getImage()).into(holder.eventPicture);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<EventShort> newList){
        list = newList;
        notifyDataSetChanged();
    }
}

class EventListViewHolder extends RecyclerView.ViewHolder{
    TextView eventTitle, eventCity, eventDate, eventFollowers;
    ImageView eventPicture;
    Button eventFollow;

    public EventListViewHolder(@NonNull View itemView) {
        super(itemView);
        eventTitle = itemView.findViewById(R.id.event_title);
        eventCity = itemView.findViewById(R.id.event_city);
        eventDate = itemView.findViewById(R.id.event_date);
        eventFollowers = itemView.findViewById(R.id.event_followers);
        eventPicture = itemView.findViewById(R.id.event_image);
        eventFollow = itemView.findViewById(R.id.event_btn_follow);
    }
}

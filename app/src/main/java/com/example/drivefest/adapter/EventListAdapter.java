package com.example.drivefest.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.drivefest.R;
import com.example.drivefest.data.model.EventShort;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class EventListAdapter extends RecyclerView.Adapter<EventListViewHolder>{
    private Context context;
    private List<EventShort> list;
    private ClickListener clickListener;
    private String sortBy;

    public EventListAdapter(Context context, List<EventShort> list, ClickListener listener) {
        this.context = context;
        this.list = list;
        clickListener = listener;
        sortBy = "xdefault";
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
        Glide
                .with(context)
                .load(list.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.ic_cloud_download)
                .error(R.drawable.ic_error)
                .into(holder.eventPicture);
        if(list.get(position).getIsFollowed()) {
            Log.e("adapter", "przycosl");
            setBtnUnFollowed(holder);
        }

        int pos = position;
        holder.listElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(list.get(pos).getId());
            }
        });
        holder.eventFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.eventFollow.getText().equals("Obserwuj")) {
                   setBtnFollowed(holder);
                }
                else if(holder.eventFollow.getText().equals("Obserwujesz")){
                    setBtnUnFollowed(holder);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<EventShort> newList){
        list.clear();
        list.addAll(newList);
        Log.e("dibug adapter", String.valueOf(list.size()));
        sortList(list);
        notifyDataSetChanged();
    }

    public void sortList(List<EventShort> list){
        if(sortBy.equals("xdefault"))
            return;

        String sortParam = sortBy.substring(3);//namedatefollowcity
        String sortOrder = sortBy.substring(0, 3);
        Comparator<EventShort> comparator = null;
        Collator collator = Collator.getInstance(new Locale("pl", "PL"));
        switch (sortParam) {
            case "name":
                comparator = Comparator.comparing(EventShort::getName, collator);
                break;
            case "date":
                comparator = Comparator.comparing(EventShort::getDate);
                break;
            case "follow":
                comparator = Comparator.comparing(EventShort::getFollowersCount);
                break;
            case "city":
                comparator = Comparator.comparing(EventShort::getLocation, collator);
                break;
        }

        if(sortOrder.equals("asc"))
            list.sort(comparator);
        else
            list.sort(comparator.reversed());
    }

    public void updateSortCriteria(String sortBy){
        this.sortBy = sortBy;
        sortList(list);
        notifyDataSetChanged();
    }

    private void setBtnUnFollowed(EventListViewHolder holder){
        holder.eventFollow.setText("Obserwuj");
        Drawable newIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite);
        holder.eventFollow.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
        holder.eventFollow.setBackgroundResource(R.drawable.custom_button);
        holder.eventFollow.setTextColor(ContextCompat.getColor(context, R.color.black));
    }

    private void setBtnFollowed(EventListViewHolder holder){
        holder.eventFollow.setText("Obserwujesz");
        Drawable newIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite_white);
        holder.eventFollow.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
        holder.eventFollow.setBackgroundResource(R.drawable.button_pressed);
        holder.eventFollow.setTextColor(ContextCompat.getColor(context, R.color.white));
    }
}

class EventListViewHolder extends RecyclerView.ViewHolder{
    CardView listElem;
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
        listElem = itemView.findViewById(R.id.event_list_elem);
    }
}

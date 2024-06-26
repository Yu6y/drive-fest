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
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.drivefest.R;
import com.example.drivefest.data.model.EventShort;

import java.util.Comparator;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListViewHolder>{
    private Context context;
    private List<EventShort> list;
    private EventClickListener eventClickListener;
    private String sortBy;

    public EventListAdapter(Context context, List<EventShort> list, EventClickListener listener) {
        this.context = context;
        this.list = list;
        eventClickListener = listener;
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

        int pos = position;
        holder.listElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventClickListener.onClick(list.get(pos).getId());
            }
        });
        holder.eventFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.eventFollow.getText().equals("Obserwuj")) {
                    holder.eventFollow.setText("Obserwujesz");
                    Drawable newIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite_white);
                    holder.eventFollow.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                    holder.eventFollow.setBackgroundResource(R.drawable.button_pressed);
                    holder.eventFollow.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
                else if(holder.eventFollow.getText().equals("Obserwujesz")){
                    holder.eventFollow.setText("Obserwuj");
                    Drawable newIcon = ContextCompat.getDrawable(context, R.drawable.ic_favorite);
                    holder.eventFollow.setCompoundDrawablesWithIntrinsicBounds(null, null, newIcon, null);
                    holder.eventFollow.setBackgroundResource(R.drawable.custom_button);
                    holder.eventFollow.setTextColor(ContextCompat.getColor(context, R.color.black));
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
        sortList(list);
        notifyDataSetChanged();
    }

    public void sortList(List<EventShort> list){
        if(sortBy.equals("xdefault"))
            return;

        String sortParam = sortBy.substring(3);//namedatefollowcity
        String sortOrder = sortBy.substring(0, 3);
        if(sortParam.equals("name")){
            if(sortOrder.equals("asc")) {
                Comparator comparator = Comparator.comparing(EventShort::getName);
                list.sort(comparator);
                //dokonczyc, ogarnac zeby pofiltorwana lista tez mogla byc posortowana
            }
            else
                list.sort(Comparator.comparing(EventShort::getName).reversed());
        }
    }

    public void updateSortCriteria(String sortBy){
        this.sortBy = sortBy;
        sortList(list);
        notifyDataSetChanged();
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

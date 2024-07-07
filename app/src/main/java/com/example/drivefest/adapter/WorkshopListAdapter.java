package com.example.drivefest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.drivefest.R;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.model.Workshop;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class WorkshopListAdapter extends RecyclerView.Adapter<WorkshopListViewHolder>{
    private Context context;
    private List<Workshop> list;
    private ClickListener clickListener;
    private String sortBy;

    public WorkshopListAdapter(Context context, List<Workshop> list, ClickListener listener) {
        this.context = context;
        this.list = list;
        clickListener = listener;
        sortBy = "wxdefault";
    }

    @NonNull
    @Override
    public WorkshopListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkshopListViewHolder(LayoutInflater.from(context).inflate(R.layout.workshop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkshopListViewHolder holder, int position) {
        holder.title.setText(list.get(position).getName());
        String tag = "";
        String[] tagsArray = list.get(position).getTags();
        for(int i = 0; i < tagsArray.length; i++){
            tag += '#' + tagsArray[i];
            if(i != tagsArray.length - 1)
                tag += ", ";
        }
        holder.tags.setText(tag);
        holder.city.setText(list.get(position).getLocation());
        holder.rating.setText(String.valueOf(list.get(position).getRating()));
        Glide
                .with(context)
                .load(list.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.ic_cloud_download)
                .error(R.drawable.ic_error)
                .into(holder.picture);

        int pos = position;
        holder.listElem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(list.get(pos).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData(List<Workshop> newList){
        list.clear();
        list.addAll(newList);
        Log.e("dibug adapter", String.valueOf(list.size()));
        sortList(list);
        notifyDataSetChanged();
    }

    public void sortList(List<Workshop> list){
        if(sortBy.equals("wxdefault"))
            return;

        String sortParam = sortBy.substring(4);//namedatefollowcity
        String sortOrder = sortBy.substring(1, 4);
        Comparator<Workshop> comparator = null;
        Collator collator = Collator.getInstance(new Locale("pl", "PL"));


        switch (sortParam) {
            case "name":
                comparator = Comparator.comparing(Workshop::getName, collator);
                break;
            case "city":
                comparator = Comparator.comparing(Workshop::getLocation, collator);
                break;
            case "rate":
                comparator = Comparator.comparing(Workshop::getRating);
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
}
class WorkshopListViewHolder extends RecyclerView.ViewHolder{
    CardView listElem;
    TextView title, city, tags, rating;
    ImageView picture;


    public WorkshopListViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.workshop_title);
        city = itemView.findViewById(R.id.workshop_city);
        tags = itemView.findViewById(R.id.workshop_tags);
        picture = itemView.findViewById(R.id.workshop_image);
        listElem = itemView.findViewById(R.id.workshop_list_elem);
        rating = itemView.findViewById(R.id.workshop_rating);
    }
}
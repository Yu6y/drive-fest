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
import com.example.drivefest.data.model.Comment;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListViewHolder>{
    private Context context;
    private List<Comment> list;

    public CommentListAdapter(Context context, List<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentListViewHolder(LayoutInflater.from(context).inflate(R.layout.comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListViewHolder holder, int position) {
        Log.e("debug timestamp",String.valueOf( list.get(position).getTimestamp().toDate()));
        Glide
                .with(context)
                .load(list.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.ic_cloud_download)
                .error(R.drawable.ic_error)
                .into(holder.userPicture);
        holder.userName.setText(list.get(position).getUserName());
        holder.content.setText(list.get(position).getContent());
        holder.time.setText(formatTimestamp(list.get(position).getTimestamp()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String formatTimestamp(Timestamp timestamp) {
        long millis = timestamp.getSeconds() * 1000 + timestamp.getNanoseconds() / 1000000;
        Date date = new Date(millis);

        // Utworzenie formatera dla daty
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC+2"));

        // Formatowanie daty na String
        return dateFormat.format(date);
    }

    public void updateData(List<Comment> newList){
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
    public void updateList(Comment comment){
        list.add(0, comment);
        notifyDataSetChanged();
    }
}

class CommentListViewHolder extends RecyclerView.ViewHolder{
    CardView listElem;
    TextView userName, content, time;
    ImageView userPicture;

    public CommentListViewHolder(@NonNull View itemView) {
        super(itemView);
        listElem = itemView.findViewById(R.id.comment_list_elem);
        userName = itemView.findViewById(R.id.comment_user_name);
        content = itemView.findViewById(R.id.comment_content);
        time = itemView.findViewById(R.id.comment_time);
        userPicture = itemView.findViewById(R.id.comment_user_pic);


    }
}

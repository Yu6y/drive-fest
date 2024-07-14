package com.example.drivefest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivefest.R;
import com.example.drivefest.data.model.Comment;
import com.example.drivefest.data.model.EventShort;
import com.example.drivefest.data.model.Expense;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListViewHolder>{
    private Context context;
    private List<Expense> list;
    private ClickExpenseListener btnClickListener;

    public ExpenseListAdapter(Context context, List<Expense> list, ClickExpenseListener btnClickListener) {
        this.context = context;
        this.list = list;
        Comparator<Expense> comparator = null;
        comparator = Comparator.comparing(Expense::getDate);
        list.sort(comparator.reversed());
        this.btnClickListener = btnClickListener;
    }

    @NonNull
    @Override
    public ExpenseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ExpenseListViewHolder(LayoutInflater.from(context).inflate(R.layout.expense, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseListViewHolder holder, int position) {
        switch(list.get(position).getType()) {
            case "fuel":
                holder.picture.setImageResource(R.drawable.ic_fuel);
                holder.type.setText("Tankowanie");
                break;
            case "service":
                holder.picture.setImageResource(R.drawable.ic_service);
                holder.type.setText("Serwis");
                break;
            case "parking":
                holder.picture.setImageResource(R.drawable.ic_parking);
                holder.type.setText("Parking");
                break;
            case "other":
                holder.picture.setImageResource(R.drawable.ic_expenses);
                holder.type.setText("Inne");
                break;
        }
        holder.price.setText("Kwota: " + list.get(position).getPrice() + " zł");
        holder.content.setText(list.get(position).getDescription());
        holder.date.setText(list.get(position).getDateStr().substring(0, 5));

        int pos = position;
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickListener.onClick(list.get(pos).getId(), 0);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClickListener.onClick(list.get(pos).getId(), 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void updateData(List<Expense> newList){
        list.clear();
        list.addAll(newList);
        Comparator<Expense> comparator = null;
        comparator = Comparator.comparing(Expense::getDate);
        list.sort(comparator.reversed());
        notifyDataSetChanged();
    }
    public void updateList(Expense expense){
        if(!list.isEmpty())
            list.add(0, expense);
        else {
            list = new ArrayList<>();
            list.add(expense);
        }
        Comparator<Expense> comparator = null;
        comparator = Comparator.comparing(Expense::getDate);
        list.sort(comparator.reversed());
        notifyDataSetChanged();
    }
}
class ExpenseListViewHolder extends RecyclerView.ViewHolder{
    CardView listElem;
    TextView type, content, date, price, update, delete;
    ImageView picture;

    public ExpenseListViewHolder(@NonNull View itemView) {
        super(itemView);
        listElem = itemView.findViewById(R.id.expense_list_elem);
        type = itemView.findViewById(R.id.list_expense_type);
        content = itemView.findViewById(R.id.list_expense_desc);
        date = itemView.findViewById(R.id.list_expense_date);
        price = itemView.findViewById(R.id.list_expense_price);
        picture = itemView.findViewById(R.id.list_exprense_icon);
        update = itemView.findViewById(R.id.list_expense_update);
        delete = itemView.findViewById(R.id.list_expense_delete);

    }
}

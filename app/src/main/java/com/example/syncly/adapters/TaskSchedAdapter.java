package com.example.syncly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.models.TaskSchedData;
import com.example.syncly.viewholders.TaskSchedViewHolder;

import java.util.List;

public class TaskSchedAdapter extends RecyclerView.Adapter<TaskSchedViewHolder> {
    Context context;
    List<TaskSchedData> items;

    public TaskSchedAdapter(Context context, List<TaskSchedData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public TaskSchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskSchedViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task_sched, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskSchedViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.descView.setText(items.get(position).getDescription());
        holder.dateView.setText(items.get(position).getDue_date().toString());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateData() {
        notifyDataSetChanged();
    }
}

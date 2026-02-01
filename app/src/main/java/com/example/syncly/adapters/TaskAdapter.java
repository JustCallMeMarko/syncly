package com.example.syncly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.models.TaskModel;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    ArrayList<TaskModel> list;

    public TaskAdapter(ArrayList<TaskModel> list) {
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc, due;

        ViewHolder(View v) {
            super(v);
            desc = v.findViewById(R.id.taskDesc);
            due = v.findViewById(R.id.taskDue);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_task, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        h.desc.setText(list.get(pos).getDescription());
        h.due.setText(list.get(pos).getDueDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}




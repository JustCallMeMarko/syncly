package com.example.syncly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.models.ScheduleModel;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    ArrayList<ScheduleModel> list;

    public ScheduleAdapter(ArrayList<ScheduleModel> list) {
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc, date;

        ViewHolder(View v) {
            super(v);
            desc = v.findViewById(R.id.schedDesc);
            date = v.findViewById(R.id.schedDate);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_schedule, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        h.desc.setText(list.get(pos).getDescription());
        h.date.setText(list.get(pos).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

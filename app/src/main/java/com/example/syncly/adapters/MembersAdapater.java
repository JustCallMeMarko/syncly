package com.example.syncly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.models.MembersModel;
import com.example.syncly.models.ScheduleModel;

import java.util.ArrayList;

public class MembersAdapater extends RecyclerView.Adapter<MembersAdapater.ViewHolder>{
    ArrayList<MembersModel> list;

    public MembersAdapater(ArrayList<MembersModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_members, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MembersModel space = list.get(position);
        holder.name.setText(space.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
        }
    }
}

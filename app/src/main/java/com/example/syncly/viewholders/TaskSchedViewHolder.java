package com.example.syncly.viewholders;
import com.example.syncly.R;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskSchedViewHolder extends RecyclerView.ViewHolder {
    public TextView nameView;
    public TextView descView;
    public TextView dateView;
    public TaskSchedViewHolder(@NonNull View itemView) {
        super(itemView);
        nameView = itemView.findViewById(R.id.nameView);
        descView =  itemView.findViewById(R.id.descView);
        dateView =  itemView.findViewById(R.id.dateView);
    }
}

package com.example.syncly.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public TextView textMessage;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        textMessage = itemView.findViewById(R.id.textMessage);
    }
}

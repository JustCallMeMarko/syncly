package com.example.syncly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.models.MessageModel;
import com.example.syncly.viewholders.ChatViewHolder;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<MessageModel> messageList;

    public ChatAdapter(List<MessageModel> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.textMessage.setText(messageList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}


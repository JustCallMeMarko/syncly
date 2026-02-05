package com.example.syncly.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.activities.SpacesHome;
import com.example.syncly.models.SpacesModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class SpacesAdapter extends RecyclerView.Adapter<SpacesAdapter.SpaceViewHolder> {

    ArrayList<SpacesModel> spaces;

    public SpacesAdapter(ArrayList<SpacesModel> spaces) {
        this.spaces = spaces;
    }

    @NonNull
    @Override
    public SpaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_space, parent, false);
        return new SpaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpaceViewHolder holder, int position) {
        SpacesModel space = spaces.get(position);

        holder.spaceName.setText(space.getName());
        holder.deadlineText.setText("Deadline: " + space.getDeadline());

        String initials = space.getName().length() >= 2
                ? space.getName().substring(0, 2).toUpperCase()
                : space.getName().toUpperCase();
        holder.avatarText.setText(initials);

        holder.spacesBtn.setOnClickListener(v ->
        {
            Context context = v.getContext(); // Get context from the clicked view
            Intent intent = new Intent(context, SpacesHome.class);
            intent.putExtra("space_id", space.getSpaceId());
            intent.putExtra("initials", initials);
            intent.putExtra("name", space.getName());
            intent.putExtra("deadline", space.getDeadline());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return spaces.size();
    }

    static class SpaceViewHolder extends RecyclerView.ViewHolder {

        TextView avatarText, spaceName, deadlineText;
        MaterialCardView spacesBtn;

        public SpaceViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarText = itemView.findViewById(R.id.avatarText);
            spaceName = itemView.findViewById(R.id.spaceName);
            deadlineText = itemView.findViewById(R.id.deadlineText);
            spacesBtn = itemView.findViewById(R.id.spacesBtn);
        }
    }
}

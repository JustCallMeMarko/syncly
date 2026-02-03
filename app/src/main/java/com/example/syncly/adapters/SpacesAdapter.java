package com.example.syncly.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.models.SpacesModel;

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

        Uri imageUri = space.getImageUri();

        if (imageUri != null) {
            holder.avatarImage.setImageURI(imageUri);
            holder.avatarImage.setVisibility(View.VISIBLE);
            holder.avatarText.setVisibility(View.GONE);
        } else {
            String initials = space.getName().length() >= 2
                    ? space.getName().substring(0, 2).toUpperCase()
                    : space.getName().toUpperCase();

            holder.avatarText.setText(initials);
            holder.avatarText.setVisibility(View.VISIBLE);
            holder.avatarImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return spaces.size();
    }

    static class SpaceViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImage;
        TextView avatarText, spaceName, deadlineText;

        public SpaceViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarImage = itemView.findViewById(R.id.avatarImage);
            avatarText = itemView.findViewById(R.id.avatarText);
            spaceName = itemView.findViewById(R.id.spaceName);
            deadlineText = itemView.findViewById(R.id.deadlineText);
        }
    }
}

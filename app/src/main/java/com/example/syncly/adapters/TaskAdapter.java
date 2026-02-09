package com.example.syncly.adapters;

import android.content.Context;
import android.graphics.Color; // Import this for colors
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.models.TaskModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    ArrayList<TaskModel> list;
    private int textColor = 0;

    public TaskAdapter(ArrayList<TaskModel> list) {
        this.list = list;
    }

    public TaskAdapter(ArrayList<TaskModel> list, int textColor) {
        this.list = list;
        this.textColor = textColor;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc, due;
        CheckBox checkBox;
        ImageView trashBtn;

        ViewHolder(View v) {
            super(v);
            desc = v.findViewById(R.id.taskDesc);
            due = v.findViewById(R.id.taskDue);
            checkBox = v.findViewById(R.id.checkBox);
            trashBtn = v.findViewById(R.id.trashBtn);
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
        TaskModel task = list.get(pos);
        h.desc.setText(task.getName());
        h.due.setText(task.getDueDate());

        // Prevent recursive calls when scrolling
        h.checkBox.setOnCheckedChangeListener(null);
        h.checkBox.setChecked(task.isCompleted() == 1); // Assuming your model has this

        h.checkBox.setOnClickListener(v -> {
            boolean isChecked = h.checkBox.isChecked();
            int status = isChecked ? 1 : 0;

            // Call the method to update the database
            updateTaskStatus(task.getId(), status, h.itemView.getContext());
        });

        h.trashBtn.setOnClickListener(v -> {
            deleteTask(task.getId(), pos, h.itemView.getContext());
        });

        if (textColor != 0) {
            h.desc.setTextColor(textColor);
            h.due.setTextColor(textColor);
            h.trashBtn.setColorFilter(textColor);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private void updateTaskStatus(int taskId, int status, Context context) {
        String url = "http://10.0.2.2/syncly/UpdateTask.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                },
                error -> {
                    Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("task_id", String.valueOf(taskId));
                params.put("status", String.valueOf(status));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }
    private void deleteTask(int taskId, int position, Context context) {
        String url = "http://10.0.2.2/syncly/DeleteTask.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, list.size());
                 },
                error -> {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("task_id", String.valueOf(taskId));
                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }
}
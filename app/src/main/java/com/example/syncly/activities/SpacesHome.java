package com.example.syncly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.adapters.TaskAdapter;
import com.example.syncly.models.TaskModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpacesHome extends AppCompatActivity {
    LinearLayout backBtn;
    CardView membersCard, folderCard;
    Button taskBtn;
    TextView initials, name, deadline, members;
    ArrayList<TaskModel> tasks = new ArrayList<>();
    RecyclerView recyclerView;
    TaskAdapter adapter;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spaces_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backBtn = findViewById(R.id.backBtn);
        membersCard = findViewById(R.id.membersCard);
        folderCard = findViewById(R.id.folderCard);
        taskBtn = findViewById(R.id.taskBtn);
        initials = findViewById(R.id.initials);
        name = findViewById(R.id.name);
        deadline = findViewById(R.id.deadline);
        members = findViewById(R.id.members);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = Volley.newRequestQueue(this); // Initialize once

        // Setup RecyclerView once
        adapter = new TaskAdapter(tasks, Color.WHITE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchTasks();
        getMemCount();
        initials.setText(getIntent().getStringExtra("initials"));
        name.setText(getIntent().getStringExtra("name"));
        deadline.setText(getIntent().getStringExtra("deadline"));
        getMemCount();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        membersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpacesHome.this, Members.class);
                int id = getIntent().getIntExtra("space_id",-1);
                intent.putExtra("space_id", id);
                startActivity(intent);
            }
        });
        folderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpacesHome.this, Folder.class);
                int id = getIntent().getIntExtra("space_id",-1);
                intent.putExtra("space_id", id);
                startActivity(intent);
            }
        });
        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpacesHome.this, Task.class);
                intent.putExtra("is_space", true);
                int id = getIntent().getIntExtra("space_id",-1);
                intent.putExtra("space_id", id);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        fetchTasks();
    }
    private void getMemCount() {
        String URL = "http://10.0.2.2/syncly/ReadMemCount.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            int count = jsonObject.getInt("member_count");
                            members.setText(String.valueOf(count));
                        }
                    } catch (JSONException e) {
                        Log.e("JSON_PARSE", "Error: " + e.getMessage());
                    }
                },
                error -> Log.e("VOLLEY_ERROR", "Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                int id = getIntent().getIntExtra("space_id", -1);

                if (id != -1) {
                    params.put("space_id", String.valueOf(id));
                } else {
                    Log.e("INTENT_ERROR", "Space ID is missing from Intent");
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void fetchTasks() {
        int spaceId = getIntent().getIntExtra("space_id", -1);
        String url = "http://10.0.2.2/syncly/GetTaskSpace.php?space_id=" + spaceId;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray arr = jsonResponse.getJSONArray("data");

                        tasks.clear(); // Clear existing list
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            tasks.add(new TaskModel(
                                    o.getInt("task_id"),
                                    o.getString("name"),
                                    o.getString("due_date"),
                                    o.getInt("status")
                            ));
                        }
                        // Notify the adapter of the change instead of re-creating it
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        Log.e("FETCH_TASKS", "JSON Error: " + e.getMessage());
                    }
                },
                err -> Toast.makeText(this, "Failed to load tasks", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(req);
    }
}
package com.example.syncly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.backend.TaskData;
import com.example.syncly.layouts.NavigationLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Task extends AppCompatActivity {

    LinearLayout backBtn;
    EditText taskName, description;
    Button createBtn, clearBtn;
    DatePicker date;
    String URL = "http://10.0.2.2/syncly/AddTask.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.backBtn);
        taskName = findViewById(R.id.taskName);
        description = findViewById(R.id.description);
        createBtn = findViewById(R.id.createBtn);
        clearBtn = findViewById(R.id.clearBtn);
        date = findViewById(R.id.date);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                        response -> {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String status = jsonResponse.getString("status");
                                if (status.equals("success")) {
                                    Toast.makeText(Task.this, "Task Added!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(Task.this, "Error: " + jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException();
                            }
                        },
                        error -> {
                            Toast.makeText(Task.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();

                        // Get values from your UI components
                        int day = date.getDayOfMonth();
                        int month = date.getMonth() + 1;
                        int year = date.getYear();
                        String dateForDatabase = String.format("%04d-%02d-%02d", year, month, day);

                        SharedPreferences sharedPreferences = getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
                        int userId = sharedPreferences.getInt("user_id", -1);

                        // Put params into the map (Strings only!)
                        params.put("name", taskName.getText().toString());
                        params.put("description", description.getText().toString());
                        params.put("id", String.valueOf(userId));
                        params.put("date", dateForDatabase);

                        return params;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(Task.this);
                queue.add(stringRequest);
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskName.setText("");
                description.setText("");
            }
        });


    }
}
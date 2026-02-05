package com.example.syncly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Task extends AppCompatActivity {

    LinearLayout backBtn;
    EditText taskName;
    Button createBtn, clearBtn;
    MaterialButton taskBtn, schedBtn;
    TextView title, descriptionTitle;
    DatePicker date;
    boolean isTask = true;
    String URL = "";
    boolean is_space = false;
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
        createBtn = findViewById(R.id.createBtn);
        clearBtn = findViewById(R.id.clearBtn);
        taskBtn = findViewById(R.id.taskBtn);
        schedBtn = findViewById(R.id.schedBtn);
        date = findViewById(R.id.date);
        title = findViewById(R.id.title);
        descriptionTitle = findViewById(R.id.descriptionTitle);
        is_space = getIntent().getBooleanExtra("is_space", false);

        if(is_space){
            schedBtn.setVisibility(View.GONE);
            title.setText("Tasks");
            descriptionTitle.setText("Create tasks for your space");
        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = taskName.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(Task.this, "Input all fields", Toast.LENGTH_LONG).show();
                    return;
                }
                if(isTask) {
                    if(is_space){
                        addSpaceTask();
                        return;
                    }
                    addTask();
                }else{
                    addSched();
                }
            }
        });
        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTask = true;
                schedBtn.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(Task.this, R.color.black)
                ));
                taskBtn.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(Task.this, R.color.green)
                ));
            }
        });
        schedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isTask = false;
                schedBtn.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(Task.this, R.color.green)
                ));
                taskBtn.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(Task.this, R.color.black)
                ));
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskName.setText("");
            }
        });


    }
    private void addTask(){
        URL = "http://10.0.2.2/syncly/AddTask.php";
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
                params.put("id", String.valueOf(userId));
                params.put("date", dateForDatabase);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Task.this);
        queue.add(stringRequest);
    }
    private void addSched(){
        URL = "http://10.0.2.2/syncly/AddSched.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        if (status.equals("success")) {
                            Toast.makeText(Task.this, "Schedule Added!", Toast.LENGTH_SHORT).show();
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

                int day = date.getDayOfMonth();
                int month = date.getMonth() + 1;
                int year = date.getYear();
                String dateForDatabase = String.format("%04d-%02d-%02d", year, month, day);

                SharedPreferences sharedPreferences = getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
                int userId = sharedPreferences.getInt("user_id", -1);

                params.put("name", taskName.getText().toString());
                params.put("id", String.valueOf(userId));
                params.put("date", dateForDatabase);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Task.this);
        queue.add(stringRequest);
    }
    private void addSpaceTask(){
        URL = "http://10.0.2.2/syncly/AddSpaceTask.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        if (status.equals("success")) {
                            Toast.makeText(Task.this, "Task Added!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.d("synclyresponse", jsonResponse.getString("message") + getIntent().getStringExtra("space_id"));
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

                int space_id = getIntent().getIntExtra("space_id", -1);

                // Put params into the map (Strings only!)
                params.put("name", taskName.getText().toString());
                params.put("space_id", String.valueOf(space_id));
                params.put("date", dateForDatabase);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Task.this);
        queue.add(stringRequest);
    }
}
package com.example.syncly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.fragments.Spaces;
import com.example.syncly.models.SpacesModel;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateSpace extends AppCompatActivity {
    EditText etSpaceName;
    DatePicker date;
    MaterialButton createBtn;
    Uri selectedImageUri = null;
    LinearLayout backBtn;
    String selectedDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_space);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etSpaceName = findViewById(R.id.etSpaceName);
        date = findViewById(R.id.date);
        createBtn = findViewById(R.id.createSpaceBtn);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> finish());
        createBtn.setOnClickListener(v -> {
            String name = etSpaceName.getText().toString().trim();
            int day = date.getDayOfMonth();
            int month = date.getMonth() + 1;
            int year = date.getYear();
            String dateForDatabase = String.format("%04d-%02d-%02d", year, month, day);

            if (name.isEmpty()) {
                return;
            }
            createSpace(name, dateForDatabase);
        });
    }
    private void createSpace(String name, String date){
        String URL = "http://10.0.2.2/syncly/AddSpace.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String status = jsonResponse.getString("status");
                        if (status.equals("success")) {
                            Toast.makeText(CreateSpace.this, "Space Created!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.d("synclyresponse", jsonResponse.getString("message"));
                            Toast.makeText(CreateSpace.this, "Error: " + jsonResponse.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException();
                    }
                },
                error -> {
                    Toast.makeText(CreateSpace.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
                int userId = sharedPreferences.getInt("user_id", -1);

                // Put params into the map (Strings only!)
                params.put("name", name);
                params.put("user_id", String.valueOf(userId));
                params.put("date", date);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(CreateSpace.this);
        queue.add(stringRequest);
    }
}
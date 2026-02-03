package com.example.syncly.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Note extends AppCompatActivity {

    LinearLayout backBtn;
    Button saveBtn;
    EditText noteEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);

        // link views
        saveBtn = findViewById(R.id.saveBtn);
        noteEt = findViewById(R.id.noteEt);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> finish());

        saveBtn.setOnClickListener(v -> {
            String noteText = noteEt.getText().toString().trim();

            if (noteText.isEmpty()) {
                Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            addNote(noteText);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void addNote(String noteText) {

        SharedPreferences sharedPreferences =
                getSharedPreferences("SynclyPrefs", Context.MODE_PRIVATE);

        int userId = sharedPreferences.getInt("user_id", -1);

        String URL = "http://10.0.2.2/syncly/AddNote.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("success")) {
                            Toast.makeText(Note.this, "Note added", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Note.this,
                                    json.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(Note.this,
                        "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(userId));
                params.put("note", noteText);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}


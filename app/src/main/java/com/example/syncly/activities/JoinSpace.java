package com.example.syncly.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JoinSpace extends AppCompatActivity {
    private LinearLayout backBtn;
    EditText c1, c2, c3, c4, c5;
    MaterialButton joinBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_space);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        joinBtn = findViewById(R.id.joinBtn);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> finish());

        joinBtn.setOnClickListener(v -> {
            String code = c1.getText().toString().trim()
                    + c2.getText().toString().trim()
                    + c3.getText().toString().trim()
                    + c4.getText().toString().trim()
                    + c5.getText().toString().trim();
            joinSpace(code);
        });
    }
    private void joinSpace(String inviteCodeInput) {
        String url = "http://10.0.2.2/syncly/JoinSpace.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        String status = json.getString("status");
                        String message = json.getString("message");

                        if (status.equals("success")) {
                            Toast.makeText(JoinSpace.this, message, Toast.LENGTH_SHORT).show();
                            // Close activity and return to the list
                            finish();
                        } else {
                            // Show error (e.g., "Invalid code" or "Already a member")
                            Toast.makeText(JoinSpace.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(JoinSpace.this, "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                err -> Toast.makeText(JoinSpace.this, "Connection error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                // Get current User ID from SharedPreferences
                SharedPreferences sp = getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
                int userId = sp.getInt("user_id", -1);

                params.put("user_id", String.valueOf(userId));
                params.put("invite_code", inviteCodeInput);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(req);
    }
}
package com.example.syncly.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.syncly.models.MembersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InviteCode extends AppCompatActivity {
    LinearLayout backBtn;
    TextView code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_invite_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backBtn = findViewById(R.id.backBtn);
        code = findViewById(R.id.code);

        getInvite();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getInvite() {
        String url = "http://10.0.2.2/syncly/GetInvite.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("syncyresponse", response);
                    try {
                        JSONObject json = new JSONObject(response);
                        String codes = json.getString("invite_code");
                        code.setText(codes);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(this, "Members error", Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                int id = getIntent().getIntExtra("space_id", -1);
                params.put("space_id", String.valueOf(id));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(req);
    }
}
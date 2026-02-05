package com.example.syncly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.adapters.MembersAdapater;
import com.example.syncly.adapters.SpacesAdapter;
import com.example.syncly.models.MembersModel;
import com.example.syncly.models.SpacesModel;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Members extends AppCompatActivity {
    public ArrayList<MembersModel> membersList = new ArrayList<>();
    RecyclerView recyclerView;
    MembersAdapater adapter;
    LinearLayout backBtn;
    MaterialButton inviteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_members);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView); // Check your XML ID!
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        // 3. INITIALIZE THE ADAPTER
        adapter = new MembersAdapater(membersList);
        recyclerView.setAdapter(adapter);
        backBtn = findViewById(R.id.backBtn);
        inviteBtn = findViewById(R.id.inviteBtn);

        inviteBtn.setOnClickListener(
                v -> {
                    Intent intent = new Intent(this, InviteCode.class);
                    int id = getIntent().getIntExtra("space_id",-1);
                    intent.putExtra("space_id", id);
                    startActivity(intent);
                }
        );
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 4. CALL THE DATA FETCH
        getMembers();
    }
    private void getMembers() {
        String url = "http://10.0.2.2/syncly/GetMembers.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("syncyresponse", response);
                    try {
                        JSONArray arr = new JSONObject(response).getJSONArray("spaces");
                        membersList.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            membersList.add(new MembersModel(
                                    o.getString("last_name") +
                                            ", " +
                                    o.getString("first_name")
                            ));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(Members.this, "Members error", Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                int id = getIntent().getIntExtra("space_id", -1);
                params.put("space_id", String.valueOf(id));
                return params;
            }
        };

        Volley.newRequestQueue(Members.this).add(req);
    }
}
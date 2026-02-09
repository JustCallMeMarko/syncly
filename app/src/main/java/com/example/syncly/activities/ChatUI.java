package com.example.syncly.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.adapters.ChatAdapter;
import com.example.syncly.models.MessageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatUI extends AppCompatActivity {
    private LinearLayout backBtn;
    private TextView nameHeader;
    private EditText messageInput;
    private CardView sendBtn;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<MessageModel> messageList;
    private ImageView imageView;
    String URL = "http://10.0.2.2/syncly/save_chat.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_ui);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backBtn = findViewById(R.id.backBtn);
        nameHeader = findViewById(R.id.name);
        messageInput = findViewById(R.id.messageInput);
        sendBtn = findViewById(R.id.sendBtn);
        recyclerView = findViewById(R.id.chatRecyclerView);
        imageView = findViewById(R.id.imageView);

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        if(getIntent().getBooleanExtra("chatbot", false)){
            nameHeader.setText("Syncly Bot");
            imageView.setImageResource(R.drawable.bot_image);
        }
        String friendName = getIntent().getStringExtra("FRIEND_NAME");
        if (friendName != null) {
            nameHeader.setText(friendName);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageInput.getText().toString().trim();
                String sender = nameHeader.getText().toString();

                if (!text.isEmpty()) {
                    messageList.add(new MessageModel(text));
                    adapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                    messageInput.setText("");

                    saveMessageToDatabase(text, sender);
                }
            }
        });

        backBtn.setOnClickListener(v -> finish());
    }

    private void saveMessageToDatabase(String message, String sender) {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DB_CON", "Server Response: " + response);
                        Toast.makeText(ChatUI.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("DB_CON", "Error: " + error.getMessage());
                        Toast.makeText(ChatUI.this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("message", message);
                params.put("sender", sender);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
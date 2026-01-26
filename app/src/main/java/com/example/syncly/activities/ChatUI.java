package com.example.syncly.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.adapters.ChatAdapter;
import com.example.syncly.models.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class ChatUI extends AppCompatActivity {
    private LinearLayout backBtn;
    private TextView nameHeader;
    private EditText messageInput;
    private CardView sendBtn;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<MessageModel> messageList;

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
        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        String friendName = getIntent().getStringExtra("FRIEND_NAME");
        if (friendName != null) {
            nameHeader.setText(friendName);
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageInput.getText().toString().trim();

                if (!text.isEmpty()) {
                    messageList.add(new MessageModel(text));
                    adapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                    messageInput.setText("");
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}


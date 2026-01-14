package com.example.syncly.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.syncly.R;
import com.example.syncly.backend.TaskData;

import java.util.Date;

public class Task extends AppCompatActivity {

    LinearLayout backBtn;
    EditText taskName, description;
    Button createBtn, clearBtn;

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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskData.getInstance().addItems(taskName.getText().toString(), description.getText().toString(), new Date());
                finish();
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
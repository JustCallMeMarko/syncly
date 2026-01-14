package com.example.syncly.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.adapters.TaskSchedAdapter;
import com.example.syncly.models.TaskSchedData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pomodoro extends AppCompatActivity {

    LinearLayout backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pomodoro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        List<TaskSchedData> items = new ArrayList<>();
        items.add(new TaskSchedData("AppDev","QUIZ 1", new Date()));
        items.add(new TaskSchedData("AppDev","QUIZ 2", new Date()));
        items.add(new TaskSchedData("Info Management","QUIZ 1", new Date()));
        items.add(new TaskSchedData("Info Management","QUIZ 2", new Date()));
        items.add(new TaskSchedData("AppDev","QUIZ 1", new Date()));
        items.add(new TaskSchedData("AppDev","QUIZ 2", new Date()));



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskSchedAdapter(getApplicationContext(),items));

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
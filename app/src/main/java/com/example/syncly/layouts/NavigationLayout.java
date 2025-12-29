package com.example.syncly.layouts;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.syncly.R;
import com.example.syncly.fragments.Chat;
import com.example.syncly.fragments.Home;
import com.example.syncly.fragments.Notif;
import com.example.syncly.fragments.Spaces;
import com.example.syncly.fragments.TaskSched;

public class NavigationLayout extends AppCompatActivity {
    ImageButton homeBtn;
    ImageButton chatBtn;
    ImageButton spacesBtn;
    ImageButton taskBtn;
    ImageButton notifBtn;

    static String selected = "home";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        homeBtn = findViewById(R.id.homeBtn);
        chatBtn = findViewById(R.id.chatBtn);
        spacesBtn = findViewById(R.id.spacesBtn);
        taskBtn = findViewById(R.id.taskBtn);
        notifBtn = findViewById(R.id.notifBtn);

        setButton();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Home(), "home");
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Chat(), "chat");
            }
        });

        spacesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Spaces(), "spaces");
            }
        });

        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new TaskSched(), "task");
            }
        });

        notifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new Notif(), "notif");
            }
        });
    }
    public void resetButtons(){
        int white = ContextCompat.getColor(this, R.color.white);
        homeBtn.setColorFilter( white );
        chatBtn.setColorFilter( white );
        spacesBtn.setColorFilter( white );
        taskBtn.setColorFilter( white );
        notifBtn.setColorFilter( white );
    }
    public void setButton(){
        resetButtons();
        int green = ContextCompat.getColor(this, R.color.mint_green);
        switch (selected) {
            case "home" :
                homeBtn.setColorFilter( green );
                break;
            case "chat" :
                chatBtn.setColorFilter( green );
                break;
            case "spaces" :
                spacesBtn.setColorFilter( green );
                break;
            case "task" :
                taskBtn.setColorFilter( green );
                break;
            case "notif" :
                notifBtn.setColorFilter( green );
                break;
        }
    }
    private void loadFragment(Fragment fragment, String tag) {
        if (selected.equals(tag)) {
            return;
        }

        selected = tag;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        setButton();
    }

}
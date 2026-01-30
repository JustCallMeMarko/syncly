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

import com.example.syncly.R;
import com.example.syncly.backend.TaskData;
import com.example.syncly.fragments.Chat;
import com.example.syncly.fragments.Drive;
import com.example.syncly.fragments.Home;
import com.example.syncly.fragments.Members;
import com.example.syncly.fragments.Spaces;
import com.example.syncly.fragments.SpacesHome;
import com.example.syncly.fragments.TaskSched;

import java.util.Date;

public class NavigationLayout extends AppCompatActivity {
    ImageButton homeBtn;
    ImageButton chatBtn;
    ImageButton spacesBtn;
    ImageButton taskBtn;

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
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                // This runs every time you go back!
                setButton();
            }
        });

        TaskData.getInstance().addItems("AppDev","QUIZ 1", new Date());
        TaskData.getInstance().addItems("AppDev","QUIZ 2", new Date());

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
    }
    public void resetButtons(){
        int white = ContextCompat.getColor(this, R.color.white);
        homeBtn.setColorFilter( white );
        chatBtn.setColorFilter( white );
        spacesBtn.setColorFilter( white );
        taskBtn.setColorFilter( white );
    }
    public void setButton() {
        resetButtons();
        int green = ContextCompat.getColor(this, R.color.green);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof Home) {
            homeBtn.setColorFilter(green);
            selected = "home";
        } else if (currentFragment instanceof Chat) {
            chatBtn.setColorFilter(green);
            selected = "chat";
        } else if (currentFragment instanceof Spaces ||
                currentFragment instanceof SpacesHome ||
                currentFragment instanceof Members ||
                currentFragment instanceof Drive) {
            spacesBtn.setColorFilter(green);
            selected = "spaces";
        } else if (currentFragment instanceof TaskSched) {
            taskBtn.setColorFilter(green);
            selected = "task";
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
                .commitNow();

        setButton();
    }

}
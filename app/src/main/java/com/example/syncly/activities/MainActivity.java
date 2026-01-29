package com.example.syncly.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.syncly.R;
import com.example.syncly.layouts.NavigationLayout;
import com.example.syncly.layouts.UserFormLayout;

public class MainActivity extends AppCompatActivity {
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        androidx.core.splashscreen.SplashScreen splashScreen =
                androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        SharedPreferences sharedPreferences = getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);

        // 3. If logged in (userId is NOT -1), redirect to your Dashboard or Home
        if (userId != -1) {
            Intent intent = new Intent(this, NavigationLayout.class); // Change this to your target activity
            startActivity(intent);
            finish(); // Close MainActivity so they can't "back" into it
            return;
        }

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserFormLayout.class);
                startActivity(intent);
            }
        });

    }
}
package com.example.syncly.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.syncly.R;
import com.example.syncly.adapters.TaskSchedAdapter;
import com.example.syncly.backend.TaskData;
import com.example.syncly.models.TaskSchedData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Pomodoro extends AppCompatActivity {

    LinearLayout backBtn;
    Button startBtn, resetBtn;
    TextView timer;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 25 * 60 * 1000;

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
        startBtn = findViewById(R.id.startBtn);
        resetBtn = findViewById(R.id.resetBtn);
        timer = findViewById(R.id.timer);
        backBtn = findViewById(R.id.backBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new TaskSchedAdapter(getApplicationContext(), TaskData.getInstance().getItems()));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                timeLeftInMillis = 25 * 60 * 1000;

                updateCountDownText();
                isTimerRunning = false;

                startBtn.setText("START");
                resetBtn.setVisibility(View.GONE);
            }
        });

    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                startBtn.setText("START");
            }
        }.start();

        isTimerRunning = true;
        resetBtn.setVisibility(View.VISIBLE);
        startBtn.setText("PAUSE");
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        startBtn.setText("RESUME");
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeLeftFormatted);
    }
}
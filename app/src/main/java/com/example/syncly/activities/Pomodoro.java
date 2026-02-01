package com.example.syncly.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.adapters.TaskAdapter;
import com.example.syncly.models.TaskModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class Pomodoro extends AppCompatActivity {
    private static final long WORK_TIME = 25 * 60 * 1000;
    private static final long BREAK_TIME = 5 * 60 * 1000;

    private LinearLayout backBtn;
    private Button startBtn, resetBtn;
    private TextView timer, modeStatus;

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private boolean isWorkMode = true;
    private long timeLeftInMillis = WORK_TIME;
    ArrayList<TaskModel> tasks = new ArrayList<>();

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
        modeStatus = findViewById(R.id.modeStatus);

        SharedPreferences sp = getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
        int userId = sp.getInt("user_id", -1);

        String url = "http://10.0.2.2/syncly/tasks.php?user_id=" + userId;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray arr = new JSONObject(response).getJSONArray("data");
                        tasks.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            tasks.add(new TaskModel(
                                    o.getInt("task_id"),
                                    o.getString("name"),
                                    o.getString("due_date"),
                                    o.getInt("status")
                            ));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setAdapter(new TaskAdapter(tasks, Color.WHITE));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(this, "Task error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(req);
        backBtn.setOnClickListener(v -> finish());

        startBtn.setOnClickListener(v -> {
            if (isTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        resetBtn.setOnClickListener(v -> resetTimer());

        updateCountDownText();
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
                toggleMode();
            }
        }.start();

        isTimerRunning = true;
        startBtn.setText("PAUSE");
        resetBtn.setVisibility(View.VISIBLE);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        startBtn.setText("RESUME");
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        isWorkMode = true;
        timeLeftInMillis = WORK_TIME;

        updateCountDownText();
        updateUI();
        startBtn.setText("START");
        resetBtn.setVisibility(View.GONE);
    }

    private void toggleMode() {
        if (isWorkMode) {
            isWorkMode = false;
            timeLeftInMillis = BREAK_TIME;
            Toast.makeText(this, "Work done! Take a break.", Toast.LENGTH_LONG).show();
        } else {
            isWorkMode = true;
            timeLeftInMillis = WORK_TIME;
            Toast.makeText(this, "Break over! Back to work.", Toast.LENGTH_LONG).show();
        }

        updateUI();
        updateCountDownText();
        startBtn.setText("START");
    }

    private void updateUI() {
        if (modeStatus != null) {
            modeStatus.setText(isWorkMode ? "FOCUS SESSION" : "BREAK TIME");
        }
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeLeftFormatted);
    }
}
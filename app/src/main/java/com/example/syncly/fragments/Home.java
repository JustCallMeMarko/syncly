package com.example.syncly.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.activities.Note;
import com.example.syncly.adapters.ScheduleAdapter;
import com.example.syncly.adapters.TaskAdapter;
import com.example.syncly.models.NotesModel;
import com.example.syncly.models.ScheduleModel;
import com.example.syncly.models.TaskModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    Button deleteBtn, addBtn;
    TextView notes, notesCounter;
    ArrayList<NotesModel> mynotes = new ArrayList<>();
    RecyclerView classSchedule, tasksViewer;
    ArrayList<ScheduleModel> schedules = new ArrayList<>();
    ArrayList<TaskModel> tasks = new ArrayList<>();

    int count = 0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        addBtn = view.findViewById(R.id.addBtn);
        notes = view.findViewById(R.id.notes);
        notesCounter = view.findViewById(R.id.notesCounter);

        getNotes();

        notes.setOnClickListener(v -> {
            if (mynotes.isEmpty()) return;
            count++;
            if (count >= mynotes.size()) {
                count = 0;
            }
            updateDisplay();
        });

        deleteBtn.setOnClickListener(v -> {
            if (mynotes.isEmpty()) {
                Toast.makeText(getActivity(), "No notes to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            int noteId = mynotes.get(count).getNote_id();
            deleteNote(noteId);
            updateDisplay();
        });

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), Note.class);
            startActivity(intent);
        });

        classSchedule = view.findViewById(R.id.classSchedule);
        tasksViewer = view.findViewById(R.id.tasksViewer);

        classSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksViewer.setLayoutManager(new LinearLayoutManager(getContext()));

        getSchedules();
        getTasks();

    }
    private void getSchedules() {
        SharedPreferences sp = requireContext()
                .getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
        int userId = sp.getInt("user_id", -1);

        String url = "http://10.0.2.2/syncly/class_sched.php?user_id=" + userId;

        StringRequest req = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray arr = new JSONObject(response).getJSONArray("data");
                        schedules.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            schedules.add(new ScheduleModel(
                                    o.getInt("sched_id"),
                                    o.getString("name"),
                                    o.getString("schedule_date")
                            ));
                        }

                        classSchedule.setAdapter(new ScheduleAdapter(schedules));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(getContext(), "Schedule error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(requireContext()).add(req);
    }

    private void getTasks() {
        SharedPreferences sp = requireContext()
                .getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
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

                        tasksViewer.setAdapter(new TaskAdapter(tasks));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(getContext(), "Task error", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(requireContext()).add(req);
    }


    private void updateDisplay() {
        // If list is empty, reset everything to default empty state
        if (mynotes == null || mynotes.isEmpty()) {
            notes.setText("No notes available");
            notesCounter.setText("0/0");
            count = 0; // Reset index
            return;
        }

        notes.setText(mynotes.get(count).getNote());
        notesCounter.setText((count + 1) + "/" + mynotes.size());
    }

    private void getNotes() {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1);
        String URL = "http://10.0.2.2/syncly/Notes.php?user_id=" + userId;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("success")) {
                            JSONArray dataArray = json.getJSONArray("data");

                            mynotes.clear();
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject noteObj = dataArray.getJSONObject(i);
                                mynotes.add(new NotesModel(
                                        noteObj.getInt("note_id"),
                                        noteObj.getString("note")
                                ));
                            }
                            count = 0;
                            updateDisplay();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void deleteNote(int noteId) {
        String URL = "http://10.0.2.2/syncly/DeleteNote.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getString("status").equals("success")) {
                            Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show();

                            // remove locally
                            mynotes.remove(count);

                            if (count >= mynotes.size() && count > 0) {
                                count--;
                            }

                            updateDisplay();
                        } else {
                            Toast.makeText(requireContext(),
                                    json.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(requireContext(),
                        "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("note_id", String.valueOf(noteId));
                return params;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
    }
}

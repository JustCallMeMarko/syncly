package com.example.syncly.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.syncly.R;

import java.util.ArrayList;

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
    ArrayList<String> mynotes = new ArrayList<>();
    int count = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deleteBtn = view.findViewById(R.id.deleteBtn);
        addBtn = view.findViewById(R.id.addBtn);
        notes = view.findViewById(R.id.notes);
        notesCounter = view.findViewById(R.id.notesCounter);

        mynotes.add("who am i");
        mynotes.add("what if?");
        mynotes.add("eh paano kung");
        mynotes.add("67");
        mynotes.add("What if bumalik ang greatest what if mo?");

        updateDisplay();

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mynotes.isEmpty()) return;

                count++;
                if (count >= mynotes.size()) {
                    count = 0;
                }
                updateDisplay();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mynotes.isEmpty()) return;

                mynotes.remove(count);

                if (count >= mynotes.size() && !mynotes.isEmpty()) {
                    count = mynotes.size() - 1;
                } else if (mynotes.isEmpty()) {
                    count = 0;
                }

                updateDisplay();
            }
        });
    }

    private void updateDisplay() {
        if (mynotes.isEmpty()) {
            notes.setText("No notes available");
            notesCounter.setText("0/0");
            return;
        }
        notes.setText(mynotes.get(count));
        notesCounter.setText((count + 1) + "/" + mynotes.size());
    }
}
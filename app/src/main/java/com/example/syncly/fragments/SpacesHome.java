package com.example.syncly.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.syncly.R;
import com.example.syncly.activities.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpacesHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpacesHome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SpacesHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpacesHome.
     */
    // TODO: Rename and change types and number of parameters
    public static SpacesHome newInstance(String param1, String param2) {
        SpacesHome fragment = new SpacesHome();
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
        return inflater.inflate(R.layout.fragment_spaces_home, container, false);
    }
    LinearLayout backBtn;
    CardView members, folder;
    Button taskBtn, chatBtn;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn = view.findViewById(R.id.backBtn);
        members = view.findViewById(R.id.members);
        folder = view.findViewById(R.id.folder);
        taskBtn = view.findViewById(R.id.taskBtn);
        chatBtn = view.findViewById(R.id.chatBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });
        members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Members()) // Replace with your container ID
                        .addToBackStack(null) // This is the "magic" line that enables the back button
                        .commit();
            }
        });
        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Drive()) // Replace with your container ID
                        .addToBackStack(null) // This is the "magic" line that enables the back button
                        .commit();
            }
        });
        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Task.class);
                startActivity(intent);
            }
        });
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SpacesHome()) // Replace with your container ID
                        .addToBackStack(null) // This is the "magic" line that enables the back button
                        .commit();
            }
        });
    }
}
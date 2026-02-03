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

import com.example.syncly.R;
import com.example.syncly.activities.JoinSpace;
import com.example.syncly.activities.Settings;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Spaces#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Spaces extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Spaces() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Spaces.
     */
    // TODO: Rename and change types and number of parameters
    public static Spaces newInstance(String param1, String param2) {
        Spaces fragment = new Spaces();
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
        return inflater.inflate(R.layout.fragment_spaces, container, false);
    }

    CardView spacesBtn;
    MaterialButton joinBtn;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spacesBtn = view.findViewById(R.id.spacesBtn);
        joinBtn = view.findViewById(R.id.joinBtn);

        spacesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SpacesHome())
                        .addToBackStack(null)
                        .commit();
            }
        });
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JoinSpace.class);
                startActivity(intent);
            }
        });
    }
}
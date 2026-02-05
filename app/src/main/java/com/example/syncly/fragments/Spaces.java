package com.example.syncly.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.activities.CreateSpace;
import com.example.syncly.activities.JoinSpace;
import com.example.syncly.activities.Settings;
import com.example.syncly.adapters.SpacesAdapter;
import com.example.syncly.adapters.TaskAdapter;
import com.example.syncly.models.SpacesModel;
import com.example.syncly.models.TaskModel;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    MaterialButton joinBtn, addBtn;
    public static ArrayList<SpacesModel> spacesList = new ArrayList<>();
    RecyclerView recyclerView;
    SpacesAdapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addBtn = view.findViewById(R.id.addBtn);
        joinBtn = view.findViewById(R.id.joinBtn);
        recyclerView = view.findViewById(R.id.spacesRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SpacesAdapter(spacesList);
        recyclerView.setAdapter(adapter);

        getSpaces();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateSpace.class);
                startActivity(intent);
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
    @Override
    public void onResume() {
        super.onResume();
        getSpaces();
    }
    private void getSpaces() {
        String url = "http://10.0.2.2/syncly/GetSpaces.php";

        StringRequest req = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray arr = new JSONObject(response).getJSONArray("spaces");
                        spacesList.clear();

                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject o = arr.getJSONObject(i);
                            spacesList.add(new SpacesModel(
                                    o.getInt("space_id"),
                                    o.getString("space_name"),
                                    o.getString("due_date")
                            ));
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                err -> Toast.makeText(getContext(), "Task error", Toast.LENGTH_SHORT).show()
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sp = requireContext()
                        .getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
                int userId = sp.getInt("user_id", -1);
                params.put("user_id", String.valueOf(userId));

                return params;
            }
        };

        Volley.newRequestQueue(requireContext()).add(req);
    }
}
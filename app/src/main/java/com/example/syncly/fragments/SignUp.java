package com.example.syncly.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;
import com.example.syncly.layouts.NavigationLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp newInstance(String param1, String param2) {
        SignUp fragment = new SignUp();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    TextView login;
    CardView errorCard;
    EditText emailInpt, passInpt, nameInpt;
    Button signupBtn;

    String URL = "http://10.0.2.2/syncly/Register.php";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login = view.findViewById(R.id.login);
        nameInpt = view.findViewById(R.id.nameInpt);
        emailInpt = view.findViewById(R.id.emailInpt);
        passInpt = view.findViewById(R.id.passInpt);
        signupBtn = view.findViewById(R.id.signupBtn);

        login.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                      .replace(R.id.fragment_container, new Login())
                      .addToBackStack(null)
                      .commit();
        });

        signupBtn.setOnClickListener(v -> {
            String name = nameInpt.getText().toString();
            String email = emailInpt.getText().toString();
            String pass = passInpt.getText().toString();

            if(email.isEmpty() || pass.isEmpty() || name.isEmpty()){
                Toast.makeText(getActivity(), "Input all fields", Toast.LENGTH_LONG).show();
                return;
            }
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(getActivity(), "Error: Input correct email", Toast.LENGTH_LONG).show();
            }
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    URL,
                    response -> {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if(status.equals("success")){
                                int idFromApi = json.getInt("user_id");
                                Intent intent = new Intent(getActivity(), NavigationLayout.class);

                                SharedPreferences prefs = getContext().getSharedPreferences("SynclyPrefs", MODE_PRIVATE);
                                prefs.edit().putInt("user_id", idFromApi).apply();

                                startActivity(intent);
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    error -> Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", name);
                    params.put("email", email);
                    params.put("password", pass);
                    return params; }
            };
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        });
    }
}
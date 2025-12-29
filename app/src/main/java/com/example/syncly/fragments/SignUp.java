package com.example.syncly.fragments;

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

import com.example.syncly.R;

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
    TextView errorMsg;
    CardView errorCard;
    EditText emailInpt;
    EditText passInpt;
    EditText usernameInpt;
    Button signupBtn;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialize Views using 'view.findViewById'
        login = view.findViewById(R.id.login);
        usernameInpt = view.findViewById(R.id.usernameInpt);
        emailInpt = view.findViewById(R.id.emailInpt);
        passInpt = view.findViewById(R.id.passInpt);
        signupBtn = view.findViewById(R.id.signupBtn);
        errorMsg = view.findViewById(R.id.errorMsg);
        errorCard = view.findViewById(R.id.errorCard);

          // 2. Signup Click Listener (Swapping Fragments)
        login.setOnClickListener(v -> {
            // Since you asked how to change fragments in the previous question:
            getParentFragmentManager().beginTransaction()
                      .replace(R.id.fragment_container, new Login()) // Replace with your actual SignUpFragment class
                      .addToBackStack(null) // Allows user to press "Back" to return to login
                      .commit();
        });

        // 3. Login Click Listener (Starting new Activity)

        signupBtn.setOnClickListener(v -> {
            String username = usernameInpt.getText().toString();
            String email = emailInpt.getText().toString();
            String pass = passInpt.getText().toString();

            if(username.isEmpty() || email.isEmpty() || pass.isEmpty()){
                errorMsg.setText("Please Input All Fields");
                errorCard.setVisibility(View.VISIBLE);
                return;
            }
        });
    }
}
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
import android.widget.EditText;
import android.widget.TextView;

import com.example.syncly.R;
import com.example.syncly.layouts.NavigationLayout;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    TextView signup, forgotPass;
    CardView errorCard;
    EditText emailInpt, passInpt;
    Button loginBtn;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signup = view.findViewById(R.id.signup);
        emailInpt = view.findViewById(R.id.emailInpt);
        passInpt = view.findViewById(R.id.passInpt);
        loginBtn = view.findViewById(R.id.loginBtn);
        forgotPass = view.findViewById(R.id.forgotPass);

        signup.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SignUp())
                    .addToBackStack(null)
                    .commit();
        });

        forgotPass.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ForgotPassword())
                    .addToBackStack(null)
                    .commit();
        });

        loginBtn.setOnClickListener(v -> {
//            String email = emailInpt.getText().toString();
//            String pass = passInpt.getText().toString();

            Intent intent = new Intent(getActivity(), NavigationLayout.class);
            startActivity(intent);
            getActivity().finish();

//            if(email.isEmpty() || pass.isEmpty()){
//                errorMsg.setText("Please Input All Fields");
//                errorCard.setVisibility(View.VISIBLE);
//                return;
//            }
//            client = new OkHttpClient();
//            RequestBody requestBody = new FormBody.Builder()
//                    .add("email", email)
//                    .add("password", pass)
//                    .build();
//            Request request = new Request.Builder()
//                    .url("http://10.0.2.2/syncly/Login.php")
//                    .post(requestBody)
//                    .build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                    getActivity().runOnUiThread(() -> {
//                        Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
//                    });
//                }
//
//                @Override
//                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                    if (response.isSuccessful()) {
//                        String jsonData = response.body().string();
//
//                        // Parse the JSON
//                        Gson gson = new Gson();
//                        UserDataModel loginResponse = gson.fromJson(jsonData, UserDataModel.class);
//
//                        getActivity().runOnUiThread(() -> {
//                            // Access the nested "status" inside "info"
//                            if (loginResponse.getInfo() != null &&
//                                    "Success".equalsIgnoreCase(loginResponse.getInfo().getStatus())) {
//
//                                Intent intent = new Intent(getActivity(), NavigationLayout.class);
//                                startActivity(intent);
//                                getActivity().finish();
//
//                            } else {
//                                errorMsg.setText("Incorrect Email/Pasword");
//                                errorCard.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                }
//            });
        });
    }
}
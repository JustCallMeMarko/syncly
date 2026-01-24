package com.example.syncly.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
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

import com.example.syncly.R;
//import com.example.syncly.activities.Task;
import com.example.syncly.layouts.NavigationLayout;


import androidx.activity.result.contract.ActivityResultContracts;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.app.Activity;

import android.util.Log;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    CallbackManager mCallbackManager;
    FirebaseAuth mAuth;
    FirebaseUser user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    GoogleSignInClient googleSignInClient;
    ActivityResultLauncher<Intent> activityResultLauncher;


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

        FacebookSdk.sdkInitialize(requireContext());



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
    Button loginBtn, googleBtn;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signup = view.findViewById(R.id.signup);
        emailInpt = view.findViewById(R.id.emailInpt);
        passInpt = view.findViewById(R.id.passInpt);
        loginBtn = view.findViewById(R.id.loginBtn);
        forgotPass = view.findViewById(R.id.forgotPass);
        googleBtn = view.findViewById(R.id.googleBtn);


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

        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task =
                                GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        handleSignInTask(task);
                    } else {
                        Toast.makeText(getActivity(), "Google sign-in cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        LoginButton loginButton = view.findViewById(R.id.facebookBtn);
        loginButton.setReadPermissions("public_profile");
        loginButton.setFragment(this);

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Facebook cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        googleBtn.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });


    }


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getActivity(), NavigationLayout.class);
                        startActivity(intent);
                        requireActivity().finish();
                    } else {
                        Toast.makeText(getActivity(), "Facebook auth failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void handleSignInTask(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            final String getFullName = account.getDisplayName();
            final String getEmail = account.getEmail();
            final String getPhotoUrl = String.valueOf(account.getPhotoUrl());


            Intent intent = new Intent(getActivity(), NavigationLayout.class);
            startActivity(intent);
            getActivity().finish();

        } catch (ApiException e) {
            Toast.makeText(getActivity(),
                    "Google Sign-In failed: " + e.getStatusCode(),
                    Toast.LENGTH_LONG
            ).show();
        }

    }
}



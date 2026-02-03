package com.example.syncly.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends Fragment {

    EditText newPassEt, confirmPassEt;
    Button resetBtn, backBtn;
    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newPassEt = view.findViewById(R.id.newPassEt);
        confirmPassEt = view.findViewById(R.id.confirmPassEt);
        resetBtn = view.findViewById(R.id.resetBtn);
        backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Login())
                        .commit()
        );

        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        resetBtn.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String newPass = newPassEt.getText().toString().trim();
        String confirmPass = confirmPassEt.getText().toString().trim();

        if (newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(getContext(), "All fields required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/syncly/reset_password.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    if (response.contains("\"status\":\"success\"")) {
                        Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();

                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new Login())
                                .commit();
                    } else {
                        Toast.makeText(getContext(), "Failed to reset password", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                params.put("new_password", newPass);
                return params;
            }
        };

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
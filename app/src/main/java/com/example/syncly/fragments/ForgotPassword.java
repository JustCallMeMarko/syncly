package com.example.syncly.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syncly.R;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends Fragment {

    EditText emailEt, o1, o2, o3, o4, o5;
    Button sendOtpBtn, verifyOtpBtn, backBtn;
    GridLayout otpContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // INPUTS
        emailEt = view.findViewById(R.id.emailEt);
        o1 = view.findViewById(R.id.otp1);
        o2 = view.findViewById(R.id.otp2);
        o3 = view.findViewById(R.id.otp3);
        o4 = view.findViewById(R.id.otp4);
        o5 = view.findViewById(R.id.otp5);

        otpContainer = view.findViewById(R.id.otpContainer);
        otpContainer.setVisibility(View.GONE);

        verifyOtpBtn = view.findViewById(R.id.verifyOtpBtn);
        verifyOtpBtn.setVisibility(View.GONE);

        sendOtpBtn = view.findViewById(R.id.sendOtpBtn);
        backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Login())
                        .commit()
        );

        sendOtpBtn.setOnClickListener(v -> sendOtp());
        verifyOtpBtn.setOnClickListener(v -> verifyOtp());
    }

    private void sendOtp() {
        String email = emailEt.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- FIX 1: DISABLE BUTTON TO PREVENT DOUBLE TAPS ---
        sendOtpBtn.setEnabled(false);
        sendOtpBtn.setText("Sending...");

        String url = "http://10.0.2.2/syncly/send_otp.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    // Re-enable button
                    sendOtpBtn.setEnabled(true);
                    sendOtpBtn.setText("Send OTP");

                    if (response.contains("success")) {
                        Toast.makeText(getContext(), "OTP sent to your email", Toast.LENGTH_SHORT).show();
                        otpContainer.setVisibility(View.VISIBLE);
                        verifyOtpBtn.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "Failed: " + response, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Re-enable button on error
                    sendOtpBtn.setEnabled(true);
                    sendOtpBtn.setText("Send OTP");
                    Toast.makeText(getContext(), "Server error or timeout", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void verifyOtp() {
        String email = emailEt.getText().toString().trim();

        // Trim every individual box before joining them
        String otp = o1.getText().toString().trim()
                + o2.getText().toString().trim()
                + o3.getText().toString().trim()
                + o4.getText().toString().trim()
                + o5.getText().toString().trim();

        // LOG THIS! Use Logcat to see if the string is exactly 5 digits
        android.util.Log.d("SynclyVerify", "Email: [" + email + "] OTP: [" + otp + "]");

        if (email.isEmpty() || otp.length() != 5) {
            Toast.makeText(getContext(), "Enter email and 5-digit OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable verify button during request
        verifyOtpBtn.setEnabled(false);

        String url = "http://10.0.2.2/syncly/verify_otp.php";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    verifyOtpBtn.setEnabled(true);
                    Log.d("SynclyResponse", "Raw Response: " + response);
                    if (response.contains("success")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", email);

                        ResetPassword resetPasswordFragment = new ResetPassword();
                        resetPasswordFragment.setArguments(bundle);
// Inside your Volley response listener
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, resetPasswordFragment)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getContext(), "Invalid or expired OTP", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    verifyOtpBtn.setEnabled(true);
                    Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("otp", otp);
                return params;
            }
        };

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
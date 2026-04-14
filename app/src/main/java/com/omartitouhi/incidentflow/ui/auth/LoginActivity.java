package com.omartitouhi.incidentflow.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.omartitouhi.incidentflow.R;
import com.omartitouhi.incidentflow.ui.dashboard.DashboardActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private ProgressBar progressBar;
    private Button loginButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();
        if (!initFirebaseClient()) {
            loginButton.setEnabled(false);
            return;
        }

        loginButton.setOnClickListener(v -> attemptLogin());

        TextView registerLink = findViewById(R.id.loginRegisterLink);
        registerLink.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        TextView forgotPasswordLink = findViewById(R.id.loginForgotPasswordLink);
        forgotPasswordLink.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth != null && auth.getCurrentUser() != null) {
            goToDashboard();
        }
    }

    private void bindViews() {
        emailInput = findViewById(R.id.loginEmailInput);
        passwordInput = findViewById(R.id.loginPasswordInput);
        progressBar = findViewById(R.id.loginProgressBar);
        loginButton = findViewById(R.id.loginButton);
    }

    private boolean initFirebaseClient() {
        try {
            auth = FirebaseAuth.getInstance();
            return true;
        } catch (IllegalStateException error) {
            Toast.makeText(this, R.string.login_firebase_not_configured, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void attemptLogin() {
        clearErrors();

        String email = valueOf(emailInput);
        String password = valueOf(passwordInput);

        if (!LoginValidator.isValidEmail(email)) {
            emailInput.setError(getString(R.string.login_email_invalid));
            emailInput.requestFocus();
            return;
        }

        if (!LoginValidator.isValidPassword(password)) {
            passwordInput.setError(getString(R.string.login_password_required));
            passwordInput.requestFocus();
            return;
        }

        setLoading(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        setLoading(false);
                        Toast.makeText(this, R.string.login_error_generic, Toast.LENGTH_LONG).show();
                        return;
                    }

                    setLoading(false);
                    goToDashboard();
                });
    }

    private void goToDashboard() {
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    private void clearErrors() {
        emailInput.setError(null);
        passwordInput.setError(null);
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!loading);
    }

    private String valueOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}



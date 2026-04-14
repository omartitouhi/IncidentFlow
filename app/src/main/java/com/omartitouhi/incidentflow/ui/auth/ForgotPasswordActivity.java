package com.omartitouhi.incidentflow.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.omartitouhi.incidentflow.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailInput;
    private ProgressBar progressBar;
    private Button resetButton;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        bindViews();
        if (!initFirebaseClient()) {
            resetButton.setEnabled(false);
            return;
        }

        resetButton.setOnClickListener(v -> requestPasswordReset());
    }

    private void bindViews() {
        emailInput = findViewById(R.id.resetEmailInput);
        progressBar = findViewById(R.id.resetProgressBar);
        resetButton = findViewById(R.id.sendResetButton);
    }

    private boolean initFirebaseClient() {
        try {
            auth = FirebaseAuth.getInstance();
            return true;
        } catch (IllegalStateException error) {
            Toast.makeText(this, R.string.forgot_password_firebase_not_configured, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    private void requestPasswordReset() {
        emailInput.setError(null);

        String email = valueOf(emailInput);
        if (ForgotPasswordValidator.isEmailEmpty(email)) {
            emailInput.setError(getString(R.string.forgot_password_email_required));
            emailInput.requestFocus();
            return;
        }

        if (!ForgotPasswordValidator.isValidEmail(email)) {
            emailInput.setError(getString(R.string.forgot_password_email_invalid));
            emailInput.requestFocus();
            return;
        }

        setLoading(true);
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        showError(task.getException() != null ? task.getException().getLocalizedMessage()
                                : getString(R.string.forgot_password_error_generic));
                        return;
                    }

                    SignInMethodQueryResult result = task.getResult();
                    boolean emailExists = result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty();
                    if (!emailExists) {
                        setLoading(false);
                        emailInput.setError(getString(R.string.forgot_password_email_not_found));
                        emailInput.requestFocus();
                        return;
                    }

                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(this, resetTask -> {
                                if (!resetTask.isSuccessful()) {
                                    showError(resetTask.getException() != null ? resetTask.getException().getLocalizedMessage()
                                            : getString(R.string.forgot_password_error_generic));
                                    return;
                                }

                                setLoading(false);
                                Toast.makeText(this, R.string.forgot_password_email_sent, Toast.LENGTH_LONG).show();
                                finish();
                            });
                });
    }

    private void showError(String message) {
        setLoading(false);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        resetButton.setEnabled(!loading);
    }

    private String valueOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}




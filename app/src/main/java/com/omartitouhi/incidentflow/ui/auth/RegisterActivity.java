package com.omartitouhi.incidentflow.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omartitouhi.incidentflow.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText fullNameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private TextInputEditText departmentInput;
    private ProgressBar progressBar;
    private Button registerButton;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();
        if (!initFirebaseClients()) {
            registerButton.setEnabled(false);
            return;
        }

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private boolean initFirebaseClients() {
        try {
            auth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();
            return true;
        } catch (IllegalStateException error) {
            Toast.makeText(this, R.string.register_firebase_not_configured, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void bindViews() {
        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        departmentInput = findViewById(R.id.departmentInput);
        progressBar = findViewById(R.id.registerProgressBar);
        registerButton = findViewById(R.id.registerButton);
    }

    private void attemptRegister() {
        clearErrors();

        String fullName = valueOf(fullNameInput);
        String email = valueOf(emailInput);
        String password = valueOf(passwordInput);
        String confirmPassword = valueOf(confirmPasswordInput);
        String department = valueOf(departmentInput);

        RegisterValidator.ValidationResult validation =
                RegisterValidator.validate(fullName, email, password, confirmPassword);

        if (!validation.isValid()) {
            showFieldError(validation);
            return;
        }

        setLoading(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        setLoading(false);
                        String error = task.getException() != null
                                ? task.getException().getLocalizedMessage()
                                : getString(R.string.register_error_generic);
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                        return;
                    }

                    FirebaseUser createdUser = task.getResult() != null ? task.getResult().getUser() : null;
                    if (createdUser == null) {
                        setLoading(false);
                        Toast.makeText(this, R.string.register_error_generic, Toast.LENGTH_LONG).show();
                        return;
                    }

                    saveProfile(createdUser.getUid(), fullName, email, department);
                });
    }

    private void saveProfile(String uid, String fullName, String email, String department) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("uid", uid);
        profile.put("fullName", fullName);
        profile.put("email", email);
        profile.put("department", TextUtils.isEmpty(department) ? null : department);
        profile.put("role", "user");
        profile.put("createdAt", FieldValue.serverTimestamp());

        firestore.collection("users")
                .document(uid)
                .set(profile)
                .addOnSuccessListener(unused -> {
                    setLoading(false);
                    Toast.makeText(this, R.string.register_success, Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(error -> {
                    rollbackCreatedUser();
                    setLoading(false);
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void rollbackCreatedUser() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            currentUser.delete();
        }
    }

    private void showFieldError(RegisterValidator.ValidationResult validation) {
        TextInputEditText target;
        switch (validation.getField()) {
            case FULL_NAME:
                target = fullNameInput;
                break;
            case EMAIL:
                target = emailInput;
                break;
            case PASSWORD:
                target = passwordInput;
                break;
            case CONFIRM_PASSWORD:
                target = confirmPasswordInput;
                break;
            default:
                target = fullNameInput;
                break;
        }

        target.setError(validation.getMessage());
        target.requestFocus();
    }

    private void clearErrors() {
        fullNameInput.setError(null);
        emailInput.setError(null);
        passwordInput.setError(null);
        confirmPasswordInput.setError(null);
        departmentInput.setError(null);
    }

    private String valueOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? ProgressBar.VISIBLE : ProgressBar.GONE);
        registerButton.setEnabled(!loading);
    }
}




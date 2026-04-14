package com.omartitouhi.incidentflow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.omartitouhi.incidentflow.ui.auth.ForgotPasswordActivity;
import com.omartitouhi.incidentflow.ui.auth.LoginActivity;
import com.omartitouhi.incidentflow.ui.auth.RegisterActivity;
import com.omartitouhi.incidentflow.ui.dashboard.DashboardActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button openRegisterButton = findViewById(R.id.openRegisterButton);
        openRegisterButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class))
        );

        Button openLoginButton = findViewById(R.id.openLoginButton);
        openLoginButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class))
        );

        Button openForgotPasswordButton = findViewById(R.id.openForgotPasswordButton);
        openForgotPasswordButton.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class))
        );
    }
}
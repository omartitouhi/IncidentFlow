package com.omartitouhi.incidentflow.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.omartitouhi.incidentflow.MainActivity;
import com.omartitouhi.incidentflow.R;
import com.omartitouhi.incidentflow.ui.auth.LoginActivity;
import com.omartitouhi.incidentflow.ui.incidents.CreateIncidentActivity;
import com.omartitouhi.incidentflow.ui.incidents.IncidentsListFragment;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        TextView welcomeText = findViewById(R.id.dashboardWelcomeText);
        Button logoutButton = findViewById(R.id.dashboardLogoutButton);
        Button createIncidentButton = findViewById(R.id.dashboardCreateIncidentButton);
        Button myIncidentsButton = findViewById(R.id.dashboardMyIncidentsButton);

        if (auth.getCurrentUser() != null && auth.getCurrentUser().getEmail() != null) {
            welcomeText.setText(getString(R.string.dashboard_welcome, auth.getCurrentUser().getEmail()));
        }

        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        createIncidentButton.setOnClickListener(v ->
                startActivity(new Intent(this, CreateIncidentActivity.class))
        );

        myIncidentsButton.setOnClickListener(v ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.dashboardFragmentContainer, new IncidentsListFragment())
                        .addToBackStack("incidents_list")
                        .commit()
        );

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.dashboardFragmentContainer, new DashboardFragment())
                    .commit();
        }
    }
}


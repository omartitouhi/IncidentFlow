package com.omartitouhi.incidentflow.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.omartitouhi.incidentflow.R;

public class DashboardFragment extends Fragment {

    private View contentContainer;
    private View emptyStateContainer;
    private View errorStateContainer;
    private ProgressBar loadingIndicator;

    private TextView totalValue;
    private TextView openValue;
    private TextView inProgressValue;
    private TextView resolvedValue;
    private TextView criticalValue;
    private TextView weatherValue;
    private TextView errorMessageValue;

    private DashboardRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
        Button retryButton = view.findViewById(R.id.dashboardRetryButton);
        retryButton.setOnClickListener(v -> loadDashboard());

        if (!initRepository()) {
            showError(getString(R.string.dashboard_firebase_not_configured));
            return;
        }

        loadDashboard();
    }

    private void bindViews(View view) {
        contentContainer = view.findViewById(R.id.dashboardContentContainer);
        emptyStateContainer = view.findViewById(R.id.dashboardEmptyContainer);
        errorStateContainer = view.findViewById(R.id.dashboardErrorContainer);
        loadingIndicator = view.findViewById(R.id.dashboardLoadingIndicator);

        totalValue = view.findViewById(R.id.dashboardTotalValue);
        openValue = view.findViewById(R.id.dashboardOpenValue);
        inProgressValue = view.findViewById(R.id.dashboardInProgressValue);
        resolvedValue = view.findViewById(R.id.dashboardResolvedValue);
        criticalValue = view.findViewById(R.id.dashboardCriticalValue);
        weatherValue = view.findViewById(R.id.dashboardWeatherValue);
        errorMessageValue = view.findViewById(R.id.dashboardErrorMessage);
    }

    private boolean initRepository() {
        try {
            repository = new DashboardRepository(FirebaseFirestore.getInstance());
            return true;
        } catch (IllegalStateException error) {
            return false;
        }
    }

    private void loadDashboard() {
        showLoading();
        repository.loadStats(new DashboardRepository.StatsCallback() {
            @Override
            public void onSuccess(DashboardStats stats) {
                if (!isAdded()) {
                    return;
                }
                if (stats.isEmpty()) {
                    showEmpty();
                    return;
                }
                renderStats(stats);
                showContent();
            }

            @Override
            public void onNetworkError() {
                if (!isAdded()) {
                    return;
                }
                showError(getString(R.string.dashboard_error_network));
            }

            @Override
            public void onError() {
                if (!isAdded()) {
                    return;
                }
                showError(getString(R.string.dashboard_error_generic));
            }
        });
    }

    private void renderStats(DashboardStats stats) {
        totalValue.setText(String.valueOf(stats.getTotalIncidents()));
        openValue.setText(String.valueOf(stats.getOpenIncidents()));
        inProgressValue.setText(String.valueOf(stats.getInProgressIncidents()));
        resolvedValue.setText(String.valueOf(stats.getResolvedIncidents()));
        criticalValue.setText(String.valueOf(stats.getCriticalIncidents()));
        weatherValue.setText(getString(R.string.dashboard_weather_placeholder));
    }

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
        emptyStateContainer.setVisibility(View.GONE);
        errorStateContainer.setVisibility(View.GONE);
    }

    private void showContent() {
        loadingIndicator.setVisibility(View.GONE);
        contentContainer.setVisibility(View.VISIBLE);
        emptyStateContainer.setVisibility(View.GONE);
        errorStateContainer.setVisibility(View.GONE);
    }

    private void showEmpty() {
        loadingIndicator.setVisibility(View.GONE);
        contentContainer.setVisibility(View.GONE);
        emptyStateContainer.setVisibility(View.VISIBLE);
        errorStateContainer.setVisibility(View.GONE);
    }

    private void showError(String message) {
        loadingIndicator.setVisibility(View.GONE);
        contentContainer.setVisibility(View.GONE);
        emptyStateContainer.setVisibility(View.GONE);
        errorStateContainer.setVisibility(View.VISIBLE);
        errorMessageValue.setText(message);
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}


package com.omartitouhi.incidentflow.ui.incidents;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omartitouhi.incidentflow.R;
import com.omartitouhi.incidentflow.data.model.IncidentListItem;
import com.omartitouhi.incidentflow.data.repository.IncidentsListRepository;
import com.omartitouhi.incidentflow.ui.adapters.IncidentListAdapter;

import java.util.List;

public class IncidentsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar loadingIndicator;
    private View emptyStateContainer;
    private View errorStateContainer;
    private TextView errorMessageView;
    private IncidentListAdapter adapter;
    private IncidentsListRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_incidents_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupRecyclerView();

        Button retryButton = view.findViewById(R.id.incidentsListRetryButton);
        retryButton.setOnClickListener(v -> loadIncidents());

        if (!initRepository()) {
            showError(getString(R.string.incident_list_firebase_not_configured));
            return;
        }

        loadIncidents();
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.incidentsListRecyclerView);
        loadingIndicator = view.findViewById(R.id.incidentsListLoadingIndicator);
        emptyStateContainer = view.findViewById(R.id.incidentsListEmptyContainer);
        errorStateContainer = view.findViewById(R.id.incidentsListErrorContainer);
        errorMessageView = view.findViewById(R.id.incidentsListErrorMessage);
    }

    private void setupRecyclerView() {
        adapter = new IncidentListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    private boolean initRepository() {
        try {
            repository = new IncidentsListRepository(FirebaseFirestore.getInstance());
            return true;
        } catch (IllegalStateException error) {
            return false;
        }
    }

    private void loadIncidents() {
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (userId == null) {
            showError(getString(R.string.incident_list_not_connected));
            return;
        }

        showLoading();
        repository.loadUserIncidents(userId, new IncidentsListRepository.Callback() {
            @Override
            public void onSuccess(List<IncidentListItem> items) {
                if (!isAdded()) {
                    return;
                }
                adapter.submitList(items);
                showContent();
            }

            @Override
            public void onEmpty() {
                if (!isAdded()) {
                    return;
                }
                adapter.submitList(null);
                showEmpty();
            }

            @Override
            public void onNetworkError() {
                if (!isAdded()) {
                    return;
                }
                showError(getString(R.string.incident_list_error_network));
            }

            @Override
            public void onError() {
                if (!isAdded()) {
                    return;
                }
                showError(getString(R.string.incident_list_error_generic));
            }
        });
    }

    private void showLoading() {
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyStateContainer.setVisibility(View.GONE);
        errorStateContainer.setVisibility(View.GONE);
    }

    private void showContent() {
        loadingIndicator.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyStateContainer.setVisibility(View.GONE);
        errorStateContainer.setVisibility(View.GONE);
    }

    private void showEmpty() {
        loadingIndicator.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyStateContainer.setVisibility(View.VISIBLE);
        errorStateContainer.setVisibility(View.GONE);
    }

    private void showError(String message) {
        loadingIndicator.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyStateContainer.setVisibility(View.GONE);
        errorStateContainer.setVisibility(View.VISIBLE);
        errorMessageView.setText(message);
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}



package com.omartitouhi.incidentflow.ui.dashboard;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardRepository {

    public interface StatsCallback {
        void onSuccess(DashboardStats stats);

        void onNetworkError();

        void onError();
    }

    private final FirebaseFirestore firestore;

    public DashboardRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void loadStats(StatsCallback callback) {
        firestore.collection("incidents")
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Map<String, Object>> incidents = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        incidents.add(doc.getData());
                    }
                    callback.onSuccess(DashboardStatsCalculator.calculate(incidents));
                })
                .addOnFailureListener(error -> {
                    if (error instanceof FirebaseFirestoreException) {
                        FirebaseFirestoreException firestoreException = (FirebaseFirestoreException) error;
                        if (firestoreException.getCode() == FirebaseFirestoreException.Code.UNAVAILABLE) {
                            callback.onNetworkError();
                            return;
                        }
                    }
                    callback.onError();
                });
    }
}


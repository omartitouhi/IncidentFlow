package com.omartitouhi.incidentflow.data.repository;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.omartitouhi.incidentflow.data.model.IncidentListItem;
import com.omartitouhi.incidentflow.data.model.IncidentListItemMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class IncidentsListRepository {

    public interface Callback {
        void onSuccess(List<IncidentListItem> items);
        void onEmpty();
        void onNetworkError();
        void onError();
    }

    private final FirebaseFirestore firestore;

    public IncidentsListRepository(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void loadUserIncidents(String userId, Callback callback) {
        firestore.collection("incidents")
                .whereEqualTo("createdBy", userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Map<String, Object>> documents = new ArrayList<>();
                    for (QueryDocumentSnapshot document : snapshot) {
                        Map<String, Object> data = document.getData();
                        if (data != null) {
                            documents.add(data);
                        }
                    }

                    sortDocumentsByCreatedAtDescending(documents);

                    List<IncidentListItem> items = new ArrayList<>();
                    for (Map<String, Object> data : documents) {
                        items.add(IncidentListItemMapper.from(data));
                    }
                    if (items.isEmpty()) {
                        callback.onEmpty();
                    } else {
                        callback.onSuccess(items);
                    }
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

    static long extractCreatedAtMillis(Object value) {
        if (value instanceof Timestamp) {
            return ((Timestamp) value).toDate().getTime();
        }
        if (value instanceof Date) {
            return ((Date) value).getTime();
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.MIN_VALUE;
    }

    static void sortDocumentsByCreatedAtDescending(List<Map<String, Object>> documents) {
        documents.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> left, Map<String, Object> right) {
                return Long.compare(
                        extractCreatedAtMillis(right.get("createdAt")),
                        extractCreatedAtMillis(left.get("createdAt"))
                );
            }
        });
    }
}


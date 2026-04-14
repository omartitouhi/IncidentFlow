package com.omartitouhi.incidentflow.data.repository;

import com.google.firebase.Timestamp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class IncidentsListRepositoryTest {

    @Test
    public void sortDocumentsByCreatedAtDescending_placesNewestFirst() {
        List<Map<String, Object>> documents = new ArrayList<>();

        documents.add(documentWithCreatedAt(new Timestamp(new Date(1_000L))));
        documents.add(documentWithCreatedAt(new Timestamp(new Date(3_000L))));
        documents.add(documentWithCreatedAt(new Timestamp(new Date(2_000L))));

        IncidentsListRepository.sortDocumentsByCreatedAtDescending(documents);

        assertEquals(3_000L, IncidentsListRepository.extractCreatedAtMillis(documents.get(0).get("createdAt")));
        assertEquals(2_000L, IncidentsListRepository.extractCreatedAtMillis(documents.get(1).get("createdAt")));
        assertEquals(1_000L, IncidentsListRepository.extractCreatedAtMillis(documents.get(2).get("createdAt")));
    }

    @Test
    public void extractCreatedAtMillis_returnsLongMinValueForUnknownTypes() {
        assertEquals(Long.MIN_VALUE, IncidentsListRepository.extractCreatedAtMillis("unknown"));
    }

    private Map<String, Object> documentWithCreatedAt(Object createdAt) {
        Map<String, Object> document = new HashMap<>();
        document.put("createdAt", createdAt);
        return document;
    }
}


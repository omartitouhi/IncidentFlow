package com.omartitouhi.incidentflow;

import com.google.firebase.Timestamp;
import com.omartitouhi.incidentflow.data.model.IncidentListItem;
import com.omartitouhi.incidentflow.data.model.IncidentListItemMapper;

import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class IncidentListItemMapperTest {

    @Test
    public void from_withExplicitLocation_andDate_returnsMappedItem() {
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Panne WiFi");
        data.put("category", "Reseau");
        data.put("priority", "High");
        data.put("status", "Open");
        data.put("location", "B - 2 - B204");
        data.put("createdAt", new Timestamp(new Date(0)));

        IncidentListItem item = IncidentListItemMapper.from(data);

        assertEquals("Panne WiFi", item.getTitle());
        assertEquals("Reseau", item.getCategory());
        assertEquals("High", item.getPriority());
        assertEquals("Open", item.getStatus());
        assertEquals("B - 2 - B204", item.getLocation());
        assertEquals(IncidentListItemMapper.resolveDateText(new Date(0)), item.getDateText());
    }

    @Test
    public void resolveLocation_withoutLocation_joinsBuildingFloorRoom() {
        Map<String, Object> data = new HashMap<>();
        data.put("building", "A");
        data.put("floor", "1");
        data.put("room", "A-101");

        assertEquals("A - 1 - A-101", IncidentListItemMapper.resolveLocation(data));
    }
}


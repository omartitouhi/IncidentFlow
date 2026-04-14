package com.omartitouhi.incidentflow;

import com.omartitouhi.incidentflow.ui.dashboard.DashboardStats;
import com.omartitouhi.incidentflow.ui.dashboard.DashboardStatsCalculator;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DashboardStatsCalculatorTest {

    @Test
    public void calculate_withNoIncidents_returnsEmptyStats() {
        DashboardStats stats = DashboardStatsCalculator.calculate(new ArrayList<>());

        assertEquals(0, stats.getTotalIncidents());
        assertTrue(stats.isEmpty());
    }

    @Test
    public void calculate_withMixedIncidents_returnsExpectedCounts() {
        List<Map<String, Object>> incidents = new ArrayList<>();

        incidents.add(incident("open", "low", null, false));
        incidents.add(incident("in_progress", "high", null, false));
        incidents.add(incident("resolved", "medium", null, false));
        incidents.add(incident("en cours", null, "critical", false));
        incidents.add(incident("ouvert", null, null, true));

        DashboardStats stats = DashboardStatsCalculator.calculate(incidents);

        assertEquals(5, stats.getTotalIncidents());
        assertEquals(2, stats.getOpenIncidents());
        assertEquals(2, stats.getInProgressIncidents());
        assertEquals(1, stats.getResolvedIncidents());
        assertEquals(3, stats.getCriticalIncidents());
    }

    private Map<String, Object> incident(String status, String priority, String severity, boolean isCritical) {
        Map<String, Object> incident = new HashMap<>();
        incident.put("status", status);
        incident.put("priority", priority);
        incident.put("severity", severity);
        incident.put("isCritical", isCritical);
        return incident;
    }
}


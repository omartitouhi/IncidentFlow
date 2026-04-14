package com.omartitouhi.incidentflow.ui.dashboard;

public class DashboardStats {

    private final int totalIncidents;
    private final int openIncidents;
    private final int inProgressIncidents;
    private final int resolvedIncidents;
    private final int criticalIncidents;

    public DashboardStats(int totalIncidents,
                          int openIncidents,
                          int inProgressIncidents,
                          int resolvedIncidents,
                          int criticalIncidents) {
        this.totalIncidents = totalIncidents;
        this.openIncidents = openIncidents;
        this.inProgressIncidents = inProgressIncidents;
        this.resolvedIncidents = resolvedIncidents;
        this.criticalIncidents = criticalIncidents;
    }

    public int getTotalIncidents() {
        return totalIncidents;
    }

    public int getOpenIncidents() {
        return openIncidents;
    }

    public int getInProgressIncidents() {
        return inProgressIncidents;
    }

    public int getResolvedIncidents() {
        return resolvedIncidents;
    }

    public int getCriticalIncidents() {
        return criticalIncidents;
    }

    public boolean isEmpty() {
        return totalIncidents == 0;
    }
}


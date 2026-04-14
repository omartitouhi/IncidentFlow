package com.omartitouhi.incidentflow.ui.dashboard;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class DashboardStatsCalculator {

    private DashboardStatsCalculator() {
    }

    public static DashboardStats calculate(List<Map<String, Object>> incidents) {
        int total = 0;
        int open = 0;
        int inProgress = 0;
        int resolved = 0;
        int critical = 0;

        for (Map<String, Object> incident : incidents) {
            total++;

            String status = readAsLower(incident.get("status"));
            String priority = readAsLower(incident.get("priority"));
            String severity = readAsLower(incident.get("severity"));
            boolean isCriticalFlag = Boolean.TRUE.equals(incident.get("isCritical"));

            if (isOpenStatus(status)) {
                open++;
            } else if (isInProgressStatus(status)) {
                inProgress++;
            } else if (isResolvedStatus(status)) {
                resolved++;
            }

            if (isCriticalFlag
                    || "critical".equals(priority)
                    || "high".equals(priority)
                    || "critical".equals(severity)
                    || "high".equals(severity)) {
                critical++;
            }
        }

        return new DashboardStats(total, open, inProgress, resolved, critical);
    }

    private static String readAsLower(Object value) {
        return value == null ? "" : value.toString().trim().toLowerCase(Locale.US);
    }

    private static boolean isOpenStatus(String status) {
        return "open".equals(status) || "ouvert".equals(status) || "new".equals(status);
    }

    private static boolean isInProgressStatus(String status) {
        return "in_progress".equals(status)
                || "in progress".equals(status)
                || "encours".equals(status)
                || "en cours".equals(status)
                || "processing".equals(status);
    }

    private static boolean isResolvedStatus(String status) {
        return "resolved".equals(status)
                || "resolu".equals(status)
                || "resolue".equals(status)
                || "closed".equals(status)
                || "ferme".equals(status)
                || "fermee".equals(status);
    }
}


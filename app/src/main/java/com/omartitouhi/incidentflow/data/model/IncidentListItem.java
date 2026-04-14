package com.omartitouhi.incidentflow.data.model;

public class IncidentListItem {

    private final String title;
    private final String category;
    private final String priority;
    private final String status;
    private final String dateText;
    private final String location;

    public IncidentListItem(String title,
                            String category,
                            String priority,
                            String status,
                            String dateText,
                            String location) {
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.dateText = dateText;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getDateText() {
        return dateText;
    }

    public String getLocation() {
        return location;
    }
}


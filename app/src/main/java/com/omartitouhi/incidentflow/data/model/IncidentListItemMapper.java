package com.omartitouhi.incidentflow.data.model;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public final class IncidentListItemMapper {

    private static final String DATE_PATTERN = "dd/MM/yyyy";

    private IncidentListItemMapper() {
    }

    public static IncidentListItem from(Map<String, Object> data) {
        String title = asString(data.get("title"));
        String category = asString(data.get("category"));
        String priority = asString(data.get("priority"));
        String status = asString(data.get("status"));
        String location = resolveLocation(data);
        String dateText = resolveDateText(data.get("createdAt"));

        return new IncidentListItem(title, category, priority, status, dateText, location);
    }

    public static String resolveLocation(Map<String, Object> data) {
        String location = asString(data.get("location"));
        if (!location.isEmpty()) {
            return location;
        }

        String building = asString(data.get("building"));
        String floor = asString(data.get("floor"));
        String room = asString(data.get("room"));
        StringBuilder builder = new StringBuilder();
        if (!building.isEmpty()) {
            builder.append(building);
        }
        if (!floor.isEmpty()) {
            if (builder.length() > 0) {
                builder.append(" - ");
            }
            builder.append(floor);
        }
        if (!room.isEmpty()) {
            if (builder.length() > 0) {
                builder.append(" - ");
            }
            builder.append(room);
        }
        return builder.length() == 0 ? "-" : builder.toString();
    }

    public static String resolveDateText(Object value) {
        Date date = null;
        if (value instanceof Timestamp) {
            date = ((Timestamp) value).toDate();
        } else if (value instanceof Date) {
            date = (Date) value;
        }

        if (date == null) {
            return "Date inconnue";
        }

        return new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(date);
    }

    private static String asString(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}


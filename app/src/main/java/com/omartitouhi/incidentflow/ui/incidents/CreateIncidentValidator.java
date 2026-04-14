package com.omartitouhi.incidentflow.ui.incidents;

public final class CreateIncidentValidator {

    private CreateIncidentValidator() {
    }

    public enum Field {
        TITLE,
        DESCRIPTION,
        CATEGORY,
        PRIORITY,
        BUILDING,
        FLOOR,
        ROOM
    }

    public static final class ValidationResult {
        private final boolean valid;
        private final Field field;
        private final String message;

        private ValidationResult(boolean valid, Field field, String message) {
            this.valid = valid;
            this.field = field;
            this.message = message;
        }

        public static ValidationResult ok() {
            return new ValidationResult(true, null, null);
        }

        public static ValidationResult error(Field field, String message) {
            return new ValidationResult(false, field, message);
        }

        public boolean isValid() {
            return valid;
        }

        public Field getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }

    public static ValidationResult validate(String title,
                                            String description,
                                            String category,
                                            String priority,
                                            String building,
                                            String floor,
                                            String room) {
        if (isBlank(title)) {
            return ValidationResult.error(Field.TITLE, "Le titre est requis.");
        }
        if (isBlank(description)) {
            return ValidationResult.error(Field.DESCRIPTION, "La description est requise.");
        }
        if (isBlank(category)) {
            return ValidationResult.error(Field.CATEGORY, "La categorie est requise.");
        }
        if (isBlank(priority)) {
            return ValidationResult.error(Field.PRIORITY, "La priorite est requise.");
        }
        if (isBlank(building)) {
            return ValidationResult.error(Field.BUILDING, "Le batiment est requis.");
        }
        if (isBlank(floor)) {
            return ValidationResult.error(Field.FLOOR, "L etage est requis.");
        }
        if (isBlank(room)) {
            return ValidationResult.error(Field.ROOM, "La salle est requise.");
        }
        return ValidationResult.ok();
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}


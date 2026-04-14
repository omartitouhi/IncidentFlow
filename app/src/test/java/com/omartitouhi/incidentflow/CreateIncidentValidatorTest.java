package com.omartitouhi.incidentflow;

import com.omartitouhi.incidentflow.ui.incidents.CreateIncidentValidator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreateIncidentValidatorTest {

    @Test
    public void validate_withAllRequiredFields_returnsSuccess() {
        CreateIncidentValidator.ValidationResult result = CreateIncidentValidator.validate(
                "Panne WiFi",
                "Le reseau est indisponible dans la salle",
                "Reseau",
                "High",
                "B",
                "2",
                "B-204"
        );

        assertTrue(result.isValid());
    }

    @Test
    public void validate_withEmptyTitle_returnsTitleError() {
        CreateIncidentValidator.ValidationResult result = CreateIncidentValidator.validate(
                "",
                "description",
                "Reseau",
                "Low",
                "A",
                "1",
                "A-101"
        );

        assertFalse(result.isValid());
        assertEquals(CreateIncidentValidator.Field.TITLE, result.getField());
    }

    @Test
    public void validate_withEmptyRoom_returnsRoomError() {
        CreateIncidentValidator.ValidationResult result = CreateIncidentValidator.validate(
                "Panne projecteur",
                "Ne s allume plus",
                "Materiel informatique",
                "Medium",
                "C",
                "3",
                ""
        );

        assertFalse(result.isValid());
        assertEquals(CreateIncidentValidator.Field.ROOM, result.getField());
    }
}

